package network;
//collect from threads SRV and client, reassemble, dissassemble
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
	 
	 public void run() {
		 
	 }

	 private void theCore(){
		 
		try {
			
			while(true){
				
				
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("FATAL: Core died");
			e.printStackTrace();
			theCore();
		}finally {
			System.err.println("FATAL: Core died, save failed");
		}
		 
	 }
}
