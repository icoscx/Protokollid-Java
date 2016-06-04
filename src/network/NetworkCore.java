package network;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import message.Message;
import message.data.type.TextMessageData;
import network.client.DatagramClient;
import network.server.DatagramServer;

//collect from threads SRV and client, reassemble, dissassemble
public class NetworkCore extends Thread{
	
	
	private Thread t;
	
	public NetworkCore(){}
	
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
				
				while(!clientDataToSend.isEmpty()){
					
					//todo
					//from some UUID db find IP+port to send to
					
					DatagramClient dc = new DatagramClient("172.16.5.240", 12344);
					dc.send(clientDataToSend.poll());
					
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
							collectable += DatagramServer.packetCache.poll().getPayloadDataAscii();
							receivedChat.add(collectable);
							collectable = "";
						}
					}
				}
				
				//Thread.sleep(1);
				
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
			System.err.println(e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.toString());
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
