package main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import network.NetworkCore;
import network.ParsingFunctions;
import network.client.DatagramClient;
import network.server.DatagramServer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

public class MainWindow{

	protected Shell shlChatV;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text DebugText;
	static DatagramServer dm = new DatagramServer(12344);
	static MainWindow window = new MainWindow();
	static NetworkCore nc = new NetworkCore();
	private Text ChatWindow;
	private Text Users;
	private Text EnterChat;
	public static String UUID = "FAFA";
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		ParsingFunctions.myUUID = UUID;
		
		dm.start();
		
		nc.start();

		window.open();

	}
	/**
	 * Open the window.
	 * @return 
	 */
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
						
						DebugText.append(dm.debugMessages.poll());
						//shlChatV.redraw();
					}
					
					if(!DatagramClient.debugMessages.isEmpty()){
						DebugText.append(DatagramClient.debugMessages.poll());
					}
				}
			});
			
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!nc.receivedChat.isEmpty()){
						ChatWindow.append(nc.receivedChat.poll());
					}	
				}
			});
			
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlChatV = new Shell();
		shlChatV.setSize(799, 477);
		shlChatV.setText("Chat v0.1");
		
		Menu menu = new Menu(shlChatV, SWT.BAR);
		shlChatV.setMenuBar(menu);
		
		Composite comp_main = new Composite(shlChatV, SWT.NONE);
		comp_main.setLocation(0, 0);
		comp_main.setSize(773, 408);
		comp_main.setLayout(null);
		
		ChatWindow = new Text(comp_main, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		ChatWindow.setEditable(false);
		ChatWindow.setBounds(10, 10, 584, 343);
		formToolkit.adapt(ChatWindow, true, true);
		
		Users = new Text(comp_main, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		Users.setEditable(false);
		Users.setBounds(602, 36, 131, 290);
		formToolkit.adapt(Users, true, true);
		
		EnterChat = new Text(comp_main, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		EnterChat.setBounds(22, 364, 515, 21);
		formToolkit.adapt(EnterChat, true, true);
		
		Button SubmitChat = new Button(comp_main, SWT.NONE);
		SubmitChat.setBounds(543, 360, 75, 25);
		formToolkit.adapt(SubmitChat, true, true);
		SubmitChat.setText("Send");
		
		Label lblUUIDLabel = formToolkit.createLabel(comp_main, "Your UUID: ", SWT.NONE);
		lblUUIDLabel.setBounds(624, 364, 95, 15);
		
		Label lblUUID = formToolkit.createLabel(comp_main, "empty", SWT.NONE);
		lblUUID.setText(UUID);
		lblUUID.setBounds(634, 383, 73, 15);
		
		MenuItem mntmMain = new MenuItem(menu, SWT.NONE);
		mntmMain.setText("Main");
		
		MenuItem mntmDebugger = new MenuItem(menu, SWT.NONE);
		mntmDebugger.setText("Debugger");
		
		MenuItem mntmExit = new MenuItem(menu, SWT.NONE);
		mntmExit.setText("Exit");
		
		Composite comp_debugger = new Composite(shlChatV, SWT.NONE);
		comp_debugger.setLocation(14, 13);
		comp_debugger.setSize(759, 395);
		comp_debugger.setLayout(null);
		
		DebugText = new Text(comp_debugger, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		DebugText.setEditable(false);
		DebugText.setLocation(0, 0);
		DebugText.setSize(749, 385);
		formToolkit.adapt(DebugText, true, true);
		
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
				//push it to somewhere
				//EnterChat.getText()
					nc.sendChat(EnterChat.getText(), "SSSS");
				}
			}
		});

	}
}
