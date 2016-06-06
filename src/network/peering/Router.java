package network.peering;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import network.ParsingFunctions;

public class Router extends Thread{
	
	private Thread t;
	
	public static volatile Queue<Neighbour> neighbourTable = new ConcurrentLinkedQueue<Neighbour>();
	
	public static volatile Queue<Route> routingTable = new ConcurrentLinkedQueue<Route>();

	public Router() {
		// TODO Auto-generated constructor stub
		
		//add myself
		routingTable.add(new Route(ParsingFunctions.myUUID, ParsingFunctions.myUUID, 0));
	}
	
	public void start(){
	    if (t == null)
	     {
	        t = new Thread (this);
	        t.start ();
	     }
	 }
	 
	 public void run() {
		 //bsniess logic
		 
		 try {
			 
			 while (true){
				 
				 try {
					
				 } catch (Exception e) {
					// TODO: handle exception
				 }

				 Thread.sleep(2000);
			 }
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	 }

}
