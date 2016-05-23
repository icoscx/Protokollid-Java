package main;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.corba.se.impl.ior.ByteBuffer;

import jdk.nashorn.internal.ir.ThrowNode;
import message.Message;
import message.data.type.TextMessageData;
import network.Responder;
import network.client.DatagramClient;
import network.server.DatagramServer;

/**
 * 
 * @author ivo.pure
 *
 */
public class Debugger {

	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		// TODO Auto-generated method stub

		/**
	    ExecutorService service = Executors.newFixedThreadPool(2);
	    DatagramServer dm = new DatagramServer(12344);
	    service.submit(dm);
	    */
		
		DatagramServer dm = new DatagramServer(12344);
		dm.start();

		Responder responder = new Responder(dm);
		responder.start();
		
		
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
