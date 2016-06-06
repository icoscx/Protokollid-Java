package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lw.bouncycastle.util.encoders.Base64;
import main.MainWindow;
import message.Message;
import message.data.type.TextMessageData;
import network.client.DatagramClient;
import network.peering.Neighbour;
import network.peering.Router;
import network.server.DatagramServer;

//collect from threads SRV and client, reassemble, dissassemble
public class NetworkCore extends Thread{
	
	
	private Thread t;
	
	public NetworkCore(){}
	
	//public static volatile Queue<Message> authSequene = new ConcurrentLinkedQueue<Message>();
	
	//public static boolean authSequenceComplete = false;
	
	public volatile Queue<Message> clientDataToSend = new ConcurrentLinkedQueue<Message>();
	
	public volatile Queue<String> receivedChat = new ConcurrentLinkedQueue<String>();
	
	public void start(){
	    if (t == null)
	     {
	        t = new Thread (this);
	        t.start ();
	     }
	 }
	 
	 public void run() {
		 theCore();
	 }

	 private void theCore(){
		 
		try {
			
			String collectable = "";
			
			while(true){
				
				try {
					/**
					while(!authSequene.isEmpty()){
						
						DatagramClient dc = new DatagramClient(authSequenceIP, authSequencePort);
						String uuid = authSequene.peek().getDestination();
						authSequenceComplete = dc.send(authSequene.poll());
						if(!authSequenceComplete){
							//del neighbour
						}else{
							//add route
						}
					}
					*/
					
					while(!clientDataToSend.isEmpty()){
						
						//todo
						//from some UUID db find IP+port to send to
						
						for (Neighbour n : Router.neighbourTable) {
							
							if(n.getUUID().equals(clientDataToSend.peek().getDestination())){
								
								//System.out.println("uid from table: " + n.getUUID() + " uid from clientdatatosend: " + clientDataToSend.peek().getDestination());
								
								DatagramClient dc = new DatagramClient(n.getIP(), n.getPort());
								dc.send(clientDataToSend.poll());
								
							}
						}
					}
					
					while(!DatagramServer.packetCache.isEmpty()){
						
						//need check for FROM
						//chating has no init, if is chat message start building
						//start building until last segment true
						if(DatagramServer.packetCache.peek().getClass().getSimpleName().equals("TextMessageData")){
							//05 //0101  //07 //0111
							if(!(DatagramServer.packetCache.peek().getFlag().equals("05") || DatagramServer.packetCache.peek().getFlag().equals("07"))){
								collectable += DatagramServer.packetCache.poll().getPayloadDataAscii();
							}else{
								MainWindow.lastChatFrom = DatagramServer.packetCache.peek().getSource();
								collectable += DatagramServer.packetCache.poll().getPayloadDataAscii();
								receivedChat.add(collectable);
								collectable = "";
							}
						}
						
						//not recognized packet, throw away
						DatagramServer.packetCache.poll();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					MainWindow.throwQueue.add("Core error, starting new cycle");
					e.printStackTrace();
					MainWindow.throwQueue.add(e.toString());
				}
				
				Thread.sleep(1);
				
			} //end while true
			
		} catch (Exception e) {
			// TODO: handle exception
			MainWindow.throwQueue.add("FATAL: Core died, restart program");
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
		}
		 
	 }
	 /**
	  * 
	  * @param data string to send
	  * @param Destination to whom in HEX id (8)
	  */
	 public void sendChat(String data, String Destination){
		  
		 try {
			Queue<String> pieces = dissassemble(data);
			 
			 int i = 0;
			 
			 while(!pieces.isEmpty()){
				 
				 if((i % 2 == 0) && pieces.size() > 1){
					 clientDataToSend.add(new TextMessageData(ParsingFunctions.myUUID, Destination, pieces.peek().length(), pieces.peek(), false, false));
					 pieces.poll();
				 }else if((i % 2 == 1) && pieces.size() > 1){
					 clientDataToSend.add(new TextMessageData(ParsingFunctions.myUUID, Destination, pieces.peek().length(), pieces.peek(), true, false));
					 pieces.poll();
				 }else if(((i % 2 == 0)) && pieces.size() == 1){
					 clientDataToSend.add(new TextMessageData(ParsingFunctions.myUUID, Destination, pieces.peek().length(), pieces.peek(), false, true));
					 pieces.poll();
				 }else if(((i % 2 == 1)) && pieces.size() == 1){
					 clientDataToSend.add(new TextMessageData(ParsingFunctions.myUUID, Destination, pieces.peek().length(), pieces.peek(), true, true));
					 pieces.poll();
				 }
				 
				 i++;
				 
			 }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
		}
		 
	 }
	 
	 public void sendFile(String fname, String destination){

        try {
			
			try {
				File file = new File("files/" + fname);
			    FileInputStream is = new FileInputStream(file);
			    byte[] chunk = new byte[1024];
			    int chunkLen = 0;
			    while ((chunkLen = is.read(chunk)) != -1) {
			        // your code..
			    }
			    //encodedBase64 = new String(Base64.encode(bytes));
			} catch (Exception e) {
			    e.printStackTrace();
			    MainWindow.throwQueue.add(e.toString());
			}
			
			Queue<String> pieces = dissassemble(encodedBase64);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
		}
	 
	 }
	 
	 private Queue<String> dissassemble(String data){
		 
		 Queue<String> ll = new LinkedList<String>();
		 
		 int numberOfPieces = (data.length() + 86) / 87;
		 
		 if(data.length() <= 87){
			 ll.add(data);
		 }else{
		 
			 for(int i = 0; i<numberOfPieces; i++){
				 
				 if(i == (numberOfPieces - 1)){
					 ll.add(data.substring(87 * i));
				 }else{
					 ll.add(data.substring(87 * i, 87 * (i+1)));
				 }
			 }
		 }
		 return ll;
		 
	 }
}
