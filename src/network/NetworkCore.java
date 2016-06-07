package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import main.MainWindow;
import message.Message;
import message.data.type.FileData;
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
	
	public volatile Queue<String> receivedFile = new ConcurrentLinkedQueue<String>();
	
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
			Date dNow = new Date( );
			int delay = 0;
			int recvdPkts = 0;

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
								continue;
							}
						}

						if(DatagramServer.packetCache.peek().getClass().getSimpleName().equals("FileData")){

						    SimpleDateFormat ft = new SimpleDateFormat ("HH_mm_ss"); //ft.format(dNow);
						    FileOutputStream fos = new FileOutputStream("files/" + ft.format(dNow), true);
							recvdPkts++;
							String from = DatagramServer.packetCache.peek().getSource();
							//1011 0B  //1010 0A  //1001 09  //1000 08
							if(DatagramServer.packetCache.peek().getFlag().equals("0A") || DatagramServer.packetCache.peek().getFlag().equals("08")){

								fos.write(DatagramServer.packetCache.poll().getPayloadDataInBytes());
								
							}else if(DatagramServer.packetCache.peek().getFlag().equals("0B") || DatagramServer.packetCache.peek().getFlag().equals("09")){

								fos.write(DatagramServer.packetCache.poll().getPayloadDataInBytes());
								fos.close();
								System.out.println("Real recieevd packet count: " + recvdPkts);
								recvdPkts=0;
								Date old = dNow;
								dNow = new Date();
								receivedFile.add(new String("Recieved file from: " + from + " named as: files/" + ft.format(old) + " Transfer Complete!"));
								continue;
							}
						}
						
						//not recognized packet, throw away
						if(!(DatagramServer.packetCache.isEmpty()) && (delay < 1000)){
								delay++;
							}else if(!(DatagramServer.packetCache.isEmpty())){
								System.out.println("Tossed away unhandled packet: " + DatagramServer.packetCache.poll().debugString());
								delay = 0;
							}

					}//end packetcache
					if(DatagramServer.packetCache.isEmpty()){
						delay = 0;
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
	 /**
	  * 
	  * @param exact file name to send
	  * @param Destination to whom in HEX id (8)
	  */
	 public void sendFile(String fname, String destination){

        try {
			
			try {
				File file = new File("files/" + fname);
			    @SuppressWarnings("resource")
				FileInputStream is = new FileInputStream(file);
			    byte[] chunk = new byte[87];
			    int chunkLen = 0;
			    //FileOutputStream fos = new FileOutputStream("files/1", true);
			    int i = 0;
			    while ((chunkLen = is.read(chunk)) != -1) {

			    	if(chunkLen == 87){
			    		//fos.write(chunk);
						 if((i % 2 == 0)){
							 clientDataToSend.add(new FileData(ParsingFunctions.myUUID, destination, chunkLen, new String(chunk, "ASCII"), false, false));
						 }else if((i % 2 == 1)){
							 clientDataToSend.add(new FileData(ParsingFunctions.myUUID, destination, chunkLen, new String(chunk, "ASCII"), true, false));
						 }

			    	}else{
			    		byte[] lastByte = new byte[87 - (87-chunkLen)];
			    		lastByte = Arrays.copyOfRange(chunk, 0, 87 - (87-chunkLen));
						 if((i % 2 == 0)){
							 clientDataToSend.add(new FileData(ParsingFunctions.myUUID, destination, chunkLen, new String(lastByte, "ASCII"), false, true));
						 }else if((i % 2 == 1)){
							 clientDataToSend.add(new FileData(ParsingFunctions.myUUID, destination, chunkLen, new String(lastByte, "ASCII"), true, true));
						 }
			    		System.out.println("last bytes; " + lastByte.length);
			    		System.out.println("TOTAL pieces sent: " + (i + 1));
			    		//fos.write(lastByte);
			    	}
			    	i++;
			    }
			    //fos.close();
			} catch (Exception e) {
			    e.printStackTrace();
			    MainWindow.throwQueue.add(e.toString());
			}
			
			
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
