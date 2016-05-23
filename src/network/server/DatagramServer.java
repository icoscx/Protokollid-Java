package network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Callable;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import message.Message;


public class DatagramServer extends Thread{
	
	protected int port = 0;
	
	private Thread t;
	
	public volatile Stack<Message> receivedPackets = new Stack<Message>();
	
	/**
	 * Port to listen. If behind NAT, Port forwarding needs to be configured.
	 * @param ListenPort
	 */
	public DatagramServer(int ListenPort){
		this.port = ListenPort;
	}
	
	 public void start(){
	     if (t == null)
	      {
	         t = new Thread (this);
	         t.start ();
	      }
	 }

	public void run() {
        try {
        	System.out.println("Listening on: " + port);
            Selector selector = Selector.open();
            DatagramChannel channel = DatagramChannel.open();
            InetSocketAddress isa = new InetSocketAddress(port);
            channel.socket().bind(isa);
            channel.configureBlocking(false);
            SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
            clientKey.attach(new Connection());
            while (true) {
                try {
                    selector.select();
                    Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        try {
                            SelectionKey key = (SelectionKey) selectedKeys.next();
                            selectedKeys.remove();

                            if (!key.isValid()) {
                              continue;
                            }

                            if (key.isReadable()) {
                            	System.out.println("Key readable, reading...");
                                read(key);
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        } catch (Exception e) {
                            System.err.println("DatagramServer: key error... " +(e.getMessage()!=null?e.getMessage():""));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("DatagramServer: selector error... " +(e.getMessage()!=null?e.getMessage():""));
                }
            }
        } catch (IOException e) {
            System.err.println("(FATAL) DatagramServer: network error: " + (e.getMessage()!=null?e.getMessage():""));
        }
    }

    private void read(SelectionKey key){
    	
        try {
			DatagramChannel chan = (DatagramChannel)key.channel();
			
			Connection con = (Connection)key.attachment();
			
			con.socketAddress = chan.receive(con.requestBuffer);
			
			int currentPosition = con.requestBuffer.position();
			if(currentPosition < 13){
				
				con.requestBuffer.clear();
				System.err.println("DatagramClient: Packet length shorter than 13 bytes");
				
			}else if(currentPosition > 100){
				
				con.requestBuffer.clear();
				System.err.println("DatagramClient: Packet length larger than 100 bytes");
				
			}else{
				
				/**
				System.out.println("Limit: " + con.requestBuffer.limit());
				System.out.println("Capacity: " + con.requestBuffer.capacity());
				System.out.println("Mark: " + con.requestBuffer.mark());
				System.out.println("Position: " + con.requestBuffer.position());
				*/
				
				con.requestBuffer.flip();
				
				byte[] bytearr = new byte[con.requestBuffer.limit()];
				
				con.requestBuffer.get(bytearr, con.requestBuffer.position(), con.requestBuffer.limit());

				Message aMessage = new Message(bytearr);
				
				aMessage.setDestinationIP(((InetSocketAddress)con.socketAddress).getHostString());
				
				aMessage.setDestinationPort(((InetSocketAddress)con.socketAddress).getPort());
				
				System.out.println("\nReceived message: "+ aMessage.toString());
				
				receivedPackets.push(aMessage);
				
				con.requestBuffer.clear();

			}
			
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		} 
    }
    /**
    private void write(SelectionKey key){
    	
        try {
			DatagramChannel chan = (DatagramChannel)key.channel();
			
			Connection con = (Connection)key.attachment();
			
			chan.send(con.responseBuffer, con.socketAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
        
    }
    */



}
