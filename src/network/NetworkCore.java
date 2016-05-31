package network;

public class NetworkCore extends Thread{
	
	private Thread t;
	
	public NetworkCore(){}
	
	 public void start(){
	     if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	 }
	 
	 public void run() {}
	

}
