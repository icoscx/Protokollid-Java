package main;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.corba.se.impl.ior.ByteBuffer;

import message.Message;
import message.data.type.TextMessageData;
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

		
	    ExecutorService service = Executors.newFixedThreadPool(2);
	    service.submit(new DatagramServer(12344));
	    //service.submit();

	    //service.shutdown();
	    //service.awaitTermination(1, TimeUnit.DAYS);

	    //System.exit(0);
	    /**
		String s = "";
		Message m1 = new TextMessageData("aaaa", "xxxx", s.length() , s, false, true);
		m1.setDestinationIP("172.16.5.240");
		m1.setDestinationPort(9876);
		System.out.println("\nSending:" + m1.toString());
		DatagramClient dc1 = new DatagramClient();
		m1 = dc1.send(m1);
		if(!dc1.hasFailed){System.out.println("\nResponseReifned:"+m1.toString());}else{System.err.println(dc1.error);}

		*/
		
	}

}
