package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import org.ini4j.Ini;
import com.didisoft.pgp.PGPKeyPair;

import lw.bouncycastle.util.encoders.Base64;
import message.Message;
import message.data.type.FileData;
import message.data.type.TextMessageData;
import network.ParsingFunctions;

/**
 * 
 * @author ivo.pure
 *
 */
public class Debugger {
	
	//static DatagramServer dm = new DatagramServer(12344);

	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		// TODO Auto-generated method stub

		/**
		String fname = "q";
		String encodedBase64 = "";
		
		try {
			File file = new File("files/" + fname);
		    FileInputStream is = new FileInputStream(file);
		    byte[] chunk = new byte[87];
		    int chunkLen = 0;
		    FileOutputStream fos = new FileOutputStream("files/1", true);
		    while ((chunkLen = is.read(chunk)) != -1) {
		        // your code..
		    	System.out.println("chunllen: " + chunkLen);
		    	if(chunkLen == 87){
		    		fos.write(chunk);
		    	}else{
		    		byte[] b = new byte[87 - (87-chunkLen)];
		    		b = Arrays.copyOfRange(chunk, 0, 87 - (87-chunkLen));
		    		System.out.println("last bytes; " + b.length);
		    		fos.write(b);
		    	}
		    }
		    fos.close();
		    //encodedBase64 = new String(Base64.encode(bytes));
		} catch (Exception e) {
		    e.printStackTrace();
		    MainWindow.throwQueue.add(e.toString());
		}
		/**
		String encodedBase64 = new String(Base64.encode("tere-dfs-sf<-f<-dsf-<df-<d-f<-sdf-<sdf-<-f<f".getBytes("ASCII")));
		System.out.println(encodedBase64);
		
		String encodedBase642 = new String(Base64.encode("tere-dfs-sf<-f<-dsf-<df-<d-f<-sdf-<sdf-<-f<f".getBytes()));
		System.out.println(encodedBase642);
		/**
		 PGPKeyPair key = new PGPKeyPair("keys/peer2");
		   System.out.println("Key ID hexadecimal");
		   System.out.println( key.getKeyIDHex());
		*/
		/**
		DatagramChannel udpchannel = DatagramChannel.open();
		DatagramSocket udpsocket = udpchannel.socket();
		SocketAddress sa = new InetSocketAddress("172.16.1.1", 5000);
		udpsocket.bind(sa);
		
		DatagramChannel udpchannel2 = DatagramChannel.open();
		DatagramSocket udpsocket2 = udpchannel2.socket();
		SocketAddress sa2 = new InetSocketAddress("172.16.1.2", 5000);
		udpsocket2.bind(sa2);
		
		/**
		 * 		
		 * 
		

		
		long start = System.currentTimeMillis();
		Thread.sleep(1000);
		long end = System.currentTimeMillis();

		System.out.println("Took : " + ((end - start)));
		

		   
		   Ini ini = new Ini(new File("config.ini"));
		   System.out.println(ini.get("config", "listeningport"));
		 
		/**String data = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		Queue<String> ll = new LinkedList<String>();
		 
		 int numberOfPieces = (data.length() + 86) / 87;
		 System.out.println("Pieces " + numberOfPieces);
		 
		 if(data.length() <= 87){
			 ll.add(data);
		 }else{
		 
			 for(int i = 0; i<numberOfPieces; i++){
				 System.out.println("Current i " + i);
				 if(i == (numberOfPieces - 1)){
					 ll.add(data.substring(87 * i));
				 }else{
					 System.out.println(87 * i + " | " + 87 * (i+1));
					 ll.add(data.substring(87 * i, 87 * (i+1)));
				 }
			 }
		 }
		/**
		System.out.println("s: " + (int) Math.ceil((double)870 / 87));
		System.out.println("s: " + (870+86)/87);
		//Collections.
		 * 
		 */
		/***
		//InitializeAuth ia = (InitializeAuth) new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes());
		
		Message m1 = new Message("aaaa", "ssss", 1, 2, 1, "1");
		InitializeAuth ia = new InitializeAuth(m1.getByteData());
		System.out.println(ia.debugString());*/
		/**
		Stack<Message> stack = new Stack<>();
		
		stack.push(new InitializeAuth("AAAA", "BBBB"));
		stack.push(new SuccessAuth("AAAA", "ssss"));
		stack.push(new ACKSQ0Control("aaaa", "BBBB"));
		stack.push(new ACKSQ1Control("AAAA", "AAAA"));
		stack.push(new FileTrasnferInitControl("AAAA", "BBBB"));
		stack.push(new KeepAliveControl("ssss", "ssss"));
		stack.push(new RoutingUpdateInitControl("ssss", "aaaa"));
		stack.push(new RSTControl("AAAA", "BBBB"));
		stack.push(new SessionInitControl("AAAA", "ssss"));
		stack.push(new FileData("AAAA", "BBBB", 1, "1", false, true));
		stack.push(new RoutingData("SSSS", "ssss", 1, "1", false, true));
		stack.push(new TextMessageData("aaaa", "ssss", 1, "1", false, true));
		stack.push(new SessionData("SSSS", "aaaa", 1, "1", false, true));
		
		stack.push(new FileData("AAAA", "BBBB", 1, "1", false, false));
		stack.push(new RoutingData("SSSS", "ssss", 1, "1", false, false));
		stack.push(new TextMessageData("aaaa", "ssss", 1, "1", false, false));
		stack.push(new SessionData("SSSS", "aaaa", 1, "1", false, false));
		
		stack.push(new FileData("AAAA", "BBBB", 1, "1", true, true));
		stack.push(new RoutingData("SSSS", "ssss", 1, "1", true, true));
		stack.push(new TextMessageData("aaaa", "ssss", 1, "1", true, true));
		stack.push(new SessionData("SSSS", "aaaa", 1, "1", true, true));
		
		stack.push(new FileData("AAAA", "BBBB", 1, "1", true, false));
		stack.push(new RoutingData("SSSS", "ssss", 1, "1", true, false));
		stack.push(new TextMessageData("aaaa", "ssss", 1, "1", true, false));
		stack.push(new SessionData("SSSS", "aaaa", 1, "1", true, false));

		 
		
		for (Message message : stack) {
			System.out.println(message.getClass().getName() + " " + message.getType() + " " + message.getFlag());
			
		}
		
		
		/**
	    ExecutorService service = Executors.newFixedThreadPool(2);
	    DatagramServer dm = new DatagramServer(12344);
	    service.submit(dm);
	    */
		
		//DatagramServer dm = new DatagramServer(12344);
		
		//dm.start();

		
		/**
	    while(true){
	    	if(!dm.receivedPackets.isEmpty()){
			    DatagramClient dc = new DatagramClient();
			    dc.send(dm.receivedPackets.pop());
	    	}
	    }
	    */
	    //service.submit();

	    //service.shutdown();
	    //service.awaitTermination(1, TimeUnit.DAYS);

	    //System.exit(0);
	    /**
	    while(true){
		String s = "";
		Message m1 = new TextMessageData("aaaa", "xxxx", s.length() , s, false, true);
		m1.setDestinationIP("172.16.5.240");
		m1.setDestinationPort(9876);
		System.out.println("\nSending:" + m1.toString());
		DatagramClient dc1 = new DatagramClient();
		m1 = dc1.send(m1);
		if(!dc1.hasFailed){
			System.out.println("\nResponseReifned:"+m1.toString());
			}else{
				System.err.println(dc1.error);
			}
		Thread.sleep(2000);
	    }
	    */
		
	    /**
	    Message m1;
	    while(true){
	    	
	    	if(!dm.receivedPackets.isEmpty()){
	    		m1 = dm.receivedPackets.pop();
	    		System.out.println("Sending: "+ m1.toString());
	    		dm.forSendingPacket.push(m1);
	    	}

	    }
	    */
	    
		
	}

}
