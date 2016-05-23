package network;

import java.util.Stack;

import message.Message;
import network.client.DatagramClient;
import network.server.DatagramServer;

public class Responder extends Thread{
	
	protected DatagramServer dm;
	
	public volatile Stack<String> data = new Stack<String>();
	
	private Thread t;
	
	public Responder(DatagramServer dm){
		this.dm = dm;
	}
	
	 public void start(){
	     if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	 }
	 
	 public void run() {

	    while(true){
	    	if(!dm.receivedPackets.isEmpty()){
			    DatagramClient dc = new DatagramClient();
			    data.push(dm.receivedPackets.peek().debugString());
			    dc.send(dm.receivedPackets.pop());
	    	}
	    }
		 
	 }

}
