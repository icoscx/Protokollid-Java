package main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import message.Message;
import network.Responder;
import network.client.DatagramClient;
import network.server.DatagramServer;

public class MainWindow{

	private Thread t;
	protected Shell shlChatV;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text DebugText;
	static DatagramServer dm = new DatagramServer(12344);
	static Responder responder = new Responder(dm);
	static MainWindow window = new MainWindow();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		//DatagramServer dm = new DatagramServer(12344);
		dm.start();
		
		//Responder responder = new Responder(dm);
		responder.start();

		window.open();
		
		//window.DebugText.append(responder.data.pop());

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
					if(!responder.data.isEmpty()){
						
						DebugText.append(responder.data.pop());
						//shlChatV.redraw();
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
		
		MenuItem mntmMain = new MenuItem(menu, SWT.NONE);
		mntmMain.setText("Main");
		
		MenuItem mntmDebugger = new MenuItem(menu, SWT.NONE);
		mntmDebugger.setText("Debugger");
		
		Composite comp_debugger = new Composite(shlChatV, SWT.NONE);
		comp_debugger.setLocation(14, 13);
		comp_debugger.setSize(759, 395);
		comp_debugger.setLayout(null);
		
		DebugText = new Text(comp_debugger, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		DebugText.setEditable(false);
		DebugText.setLocation(0, 0);
		DebugText.setSize(749, 385);
		formToolkit.adapt(DebugText, true, true);
		
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

	}
}
