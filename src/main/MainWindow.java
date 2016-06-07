package main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.ini4j.Ini;

import network.NetworkCore;
import network.ParsingFunctions;
import network.client.DatagramClient;
import network.peering.Neighbour;
import network.peering.Router;
import network.server.DatagramServer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
/**
 * 
 * @author ivo.pure karl.mendelman kristjan.room
 *
 */
public class MainWindow{

	protected Shell shlChatV;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text DebugText;
	private Text ChatWindow;
	private Text EnterChat;
	public static volatile Queue<String> throwQueue = new ConcurrentLinkedQueue<String>();
	static DatagramServer dm = new DatagramServer();
	static MainWindow window = new MainWindow();
	static NetworkCore nc = new NetworkCore();
	static Router router = new Router();
	private Text IP;
	private Text port;
	private Text uuidToAdd;
	private List UserList;
	private int realCountInNTable = 0;
	public static String lastChatFrom = "";
	private Text fileInput;
	
	public static void main(String[] args) {
		
		boolean configLoaded = false;
		
		int port = 0;
		
		String myUID = "";
		
		try {
			Ini ini = new Ini(new File("config.ini"));
			port = Integer.valueOf(ini.get("config", "listeningport"));
			myUID = ini.get("config", "myuid");
			configLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(configLoaded){
		
			ParsingFunctions.myUUID = myUID;
			dm.setPort(port);
			dm.start();	
			nc.start();
			router.start();
			window.open();
		}

	}
	
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlChatV.open();
		shlChatV.layout();

		while (!shlChatV.isDisposed()) {

			if (!display.readAndDispatch()) {
				display.sleep();
			}
			display.asyncExec(new Runnable() {//asnybc!
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!dm.debugMessages.isEmpty()){
						DebugText.append(dm.debugMessages.poll() + "\n");
					}
					
					if(!DatagramClient.debugMessages.isEmpty()){
						DebugText.append(DatagramClient.debugMessages.poll() + "\n");
					}
					
					if(!throwQueue.isEmpty()){
						DebugText.append(throwQueue.poll() + "\n");
					}
					
					if(DebugText.getText().length() > 10000){
						DebugText.setText("");
					}
				}
			});
			
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!nc.receivedChat.isEmpty()){
						Date dNow = new Date( );
					    SimpleDateFormat ft = 
					    new SimpleDateFormat ("HH:mm:ss");  
					    ChatWindow.append("[ "+ ft.format(dNow) + " ] " + lastChatFrom + ": " + nc.receivedChat.poll() + "\n");
					}	
				}
			});
			
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!nc.receivedFile.isEmpty()){
						Date dNow = new Date( );
					    SimpleDateFormat ft = 
					    new SimpleDateFormat ("HH:mm:ss");  
					    ChatWindow.append("[ "+ ft.format(dNow) + " ] SYSTEM: " + nc.receivedFile.poll() + "\n");
					}	
				}
			});
			
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ArrayList<String> currentList = new ArrayList<String>(Arrays.asList(UserList.getItems()));
					
					if(realCountInNTable != currentList.size()){
						UserList.removeAll();
					}
					
					realCountInNTable = 0;
					
					for (Neighbour n : Router.neighbourTable) {
						
						realCountInNTable++;
						
						if(!currentList.contains(n.getUUID())){
							UserList.add(n.getUUID());
						}
					}
				}
			});
			
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlChatV = new Shell();
		shlChatV.setSize(799, 477);
		shlChatV.setText("Chat v0.1.34a");
		
		Menu menu = new Menu(shlChatV, SWT.BAR);
		shlChatV.setMenuBar(menu);
		
		Composite comp_main = new Composite(shlChatV, SWT.NONE);
		comp_main.setLocation(0, 0);
		comp_main.setSize(773, 408);
		comp_main.setLayout(null);
		
		ChatWindow = new Text(comp_main, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		ChatWindow.setEditable(false);
		ChatWindow.setBounds(10, 10, 584, 308);
		formToolkit.adapt(ChatWindow, true, true);
		
		EnterChat = new Text(comp_main, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		EnterChat.setBounds(20, 328, 515, 21);
		formToolkit.adapt(EnterChat, true, true);
		
		Button SubmitChat = new Button(comp_main, SWT.NONE);
		SubmitChat.setBounds(541, 324, 75, 25);
		formToolkit.adapt(SubmitChat, true, true);
		SubmitChat.setText("Send");
		
		Label lblUUIDLabel = formToolkit.createLabel(comp_main, "Your UUID: ", SWT.NONE);
		lblUUIDLabel.setBounds(600, 13, 61, 15);
		
		Label lblUUID = formToolkit.createLabel(comp_main, "empty", SWT.NONE);
		lblUUID.setText(ParsingFunctions.myUUID);
		lblUUID.setBounds(663, 13, 73, 15);
		
		UserList = new List(comp_main, SWT.BORDER);
		UserList.setBounds(600, 51, 136, 267);
		formToolkit.adapt(UserList, true, true);
		
		Label lblAboveUsers = new Label(comp_main, SWT.NONE);
		lblAboveUsers.setBounds(600, 30, 136, 15);
		formToolkit.adapt(lblAboveUsers, true, true);
		lblAboveUsers.setText("Select User to chat WITH");
		
		IP = new Text(comp_main, SWT.BORDER);
		IP.setText("0.0.0.0");
		IP.setBounds(127, 355, 86, 21);
		formToolkit.adapt(IP, true, true);
		
		Label lblIpportToAdd = new Label(comp_main, SWT.NONE);
		lblIpportToAdd.setBounds(30, 358, 80, 15);
		formToolkit.adapt(lblIpportToAdd, true, true);
		lblIpportToAdd.setText("IP/port to add:");
		
		port = new Text(comp_main, SWT.BORDER);
		port.setText("12554");
		port.setBounds(219, 355, 50, 21);
		formToolkit.adapt(port, true, true);
		
		Button btnAddNeighbour = new Button(comp_main, SWT.NONE);
		btnAddNeighbour.setBounds(356, 355, 107, 21);
		formToolkit.adapt(btnAddNeighbour, true, true);
		btnAddNeighbour.setText("Add Neighbour");
		
		uuidToAdd = new Text(comp_main, SWT.BORDER);
		uuidToAdd.setText("FFFFFFFF");
		uuidToAdd.setBounds(281, 355, 69, 21);
		formToolkit.adapt(uuidToAdd, true, true);
		
		Label lblTransferFile = new Label(comp_main, SWT.NONE);
		lblTransferFile.setBounds(466, 385, 69, 15);
		formToolkit.adapt(lblTransferFile, true, true);
		lblTransferFile.setText("Transfer file: ");
		
		fileInput = new Text(comp_main, SWT.BORDER);
		fileInput.setText("file.exe");
		fileInput.setBounds(541, 382, 145, 21);
		formToolkit.adapt(fileInput, true, true);
		
		Label lblFilenameAndExt = new Label(comp_main, SWT.NONE);
		lblFilenameAndExt.setBounds(541, 358, 145, 15);
		formToolkit.adapt(lblFilenameAndExt, true, true);
		lblFilenameAndExt.setText("FileName and extension:");
		
		Button btnSendFile = new Button(comp_main, SWT.NONE);
		btnSendFile.setBounds(688, 380, 75, 25);
		formToolkit.adapt(btnSendFile, true, true);
		btnSendFile.setText("Send File");
		
		Button btnSendALL = new Button(comp_main, SWT.NONE);
		btnSendALL.setBounds(639, 324, 75, 25);
		formToolkit.adapt(btnSendALL, true, true);
		btnSendALL.setText("Send to ALL");
		
		
		MenuItem mntmMain = new MenuItem(menu, SWT.NONE);
		mntmMain.setText("Main");
		
		MenuItem mntmDebugger = new MenuItem(menu, SWT.NONE);
		mntmDebugger.setText("Debugger");
		
		MenuItem mntmExit = new MenuItem(menu, SWT.NONE);
		mntmExit.setText("Exit");
		
		Composite comp_debugger = new Composite(shlChatV, SWT.NONE);
		comp_debugger.setLocation(0, 0);
		comp_debugger.setSize(762, 396);
		comp_debugger.setLayout(null);
		
		DebugText = new Text(comp_debugger, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		DebugText.setEditable(false);
		DebugText.setLocation(0, 0);
		DebugText.setSize(752, 386);
		formToolkit.adapt(DebugText, true, true);
		
		btnAddNeighbour.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date dNow = new Date( );
			    SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
				try {
					if(uuidToAdd.getText().toUpperCase().equals(ParsingFunctions.myUUID)){
						throw new Exception("Cannot add yourself");
					}
					
					if(!(IP.getText().matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"))){
						throw new Exception("wrong ip address format ("+IP.getText()+")");
					}
					
					if(!(Integer.valueOf(port.getText()) > 1024) && (Integer.valueOf(port.getText()) < 65535)){
						throw new Exception("port not acceptable ("+port.getText()+")!");
					}
					
					for (Neighbour n : Router.neighbourTable) {
						if(n.getUUID().toUpperCase().equals(uuidToAdd.getText().toUpperCase())){
							throw new Exception("Dublicate entry");
						}
					}

					Router.neighbourTable.add(new Neighbour(uuidToAdd.getText(), IP.getText(), Integer.valueOf(port.getText())));
					
					
				} catch (Exception ex1) {
					// TODO Auto-generated catch block
					ex1.printStackTrace();
					MainWindow.throwQueue.add(ex1.toString());
					ChatWindow.append("[ "+ ft.format(dNow) +" ] SYSTEM: " + ex1.toString() + "\n");
				}
				IP.setText("");
				port.setText("");
				uuidToAdd.setText("");
			}
		});
		
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		
		mntmDebugger.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comp_debugger.setVisible(true);
				comp_main.setVisible(false);
			}
		});
		
		mntmMain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comp_debugger.setVisible(false);
				comp_main.setVisible(true);
			}
		});
		
		SubmitChat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(EnterChat.getText().length() > 0){
					
					Date dNow = new Date( );
				    SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss"); 
				    
					if(!(UserList.getSelectionCount() == 0)){
						
						nc.sendChat(EnterChat.getText(), UserList.getSelection()[0]);
						ChatWindow.append("[ "+ ft.format(dNow) +" ] Me: " + EnterChat.getText() + "\n");
						EnterChat.setText("");
						
					}else{
						ChatWindow.append("[ "+ ft.format(dNow) +" ] SYSTEM: Please select User to send chat to!\n");
					}
				}
			}
		});
		
		
		btnSendFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(fileInput.getText().length() > 0){
					
					Date dNow = new Date( );
				    SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
					
					if(!(UserList.getSelectionCount() == 0)){
						
						nc.sendFile(fileInput.getText(), UserList.getSelection()[0]);
						ChatWindow.append("[ "+ ft.format(dNow) +" ] Me: Sending file to " + UserList.getSelection()[0] + "\n");
						fileInput.setText("");
						
					}else{
						ChatWindow.append("[ "+ ft.format(dNow) +" ] SYSTEM: Please select User to send file to!\n");
					}
					
				}
				
			}
		});
		
		btnSendALL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Date dNow = new Date( );
			    SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
				
				if(UserList.getItemCount() > 0){
					
					ArrayList<String> currentList = new ArrayList<String>(Arrays.asList(UserList.getItems()));
					
					for (String user : currentList) {
						
						nc.sendChat(EnterChat.getText(), user);
						
					}
					
				}else{
					ChatWindow.append("[ "+ ft.format(dNow) +" ] SYSTEM: YOu need to have atleast one friend for multychat!\n");
				}
				
			}
		});

	}
}
