package network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Callable;
import message.Message;


public class DatagramServer extends Thread{
	
	protected int port = 0;
	
	private Thread t;
	
	public volatile Stack<Message> receivedPacket = new Stack<Message>();
	
	public volatile Stack<Message> forSendingPacket = new Stack<Message>();
	
	public volatile Stack<String> debugMessages = new Stack<String>();
	
	/**
	 * Port to listen. If behind NAT, Port forwarding needs to be configured.
	 * @param ListenPort
	 */
	public DatagramServer(int ListenPort){
		
		//process(ListenPort);
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
                            	System.out.println("*****Key not valid, re-do*****");
                                continue;
                            }

                            if (key.isReadable()) {
                            	System.out.println("*****Key readable, reading...*****");
                                read(key);
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                            	System.out.println("*****Key writable, writing...*****");
                                write(key);
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        } catch (Exception e) {
                            System.err.println("DatagramServer: key error... " +(e.getMessage()!=null?e.getMessage():""));
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.err.println("DatagramServer: selector error... " +(e.getMessage()!=null?e.getMessage():""));
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("(FATAL) DatagramServer: network error: " + (e.getMessage()!=null?e.getMessage():""));
            e.printStackTrace();
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

					con.requestBuffer.flip();
					/**
					System.out.println("Limit: " + con.requestBuffer.limit());
					System.out.println("Capacity: " + con.requestBuffer.capacity());
					System.out.println("Mark: " + con.requestBuffer.mark());
					System.out.println("Position: " + con.requestBuffer.position());
					*/
					byte[] bytearr = new byte[con.requestBuffer.limit()];
					
					con.requestBuffer.get(bytearr, con.requestBuffer.position(), con.requestBuffer.limit());

					Message aMessage = new Message(bytearr);
					
					//System.out.println(aMessage.toString());
					System.out.println("\nReceived message: "+ aMessage.debugString());
					
					receivedPacket.push(aMessage);
					debugMessages.push(new String("Received: \n"));
					debugMessages.push(receivedPacket.peek().debugString());

					//System.out.println("PEEK: "); 
					//System.out.println("read fun -> forSendingPacket -> " + forSendingPacket.peek().debugString());
					
					con.requestBuffer.clear();
					//forSendingPacket.push(aMessage);
					/**
					 * töötlus siia
					 */
					forSendingPacket.push(receivedPacket.pop());
					//con.responseBuffer.clear();
					//con.responseBuffer = Charset.forName( "ASCII" ).newEncoder().encode(CharBuffer.wrap("01616161617878787801090F00"));
					while(!forSendingPacket.empty()){
						//System.out.println("PEEK: "); 
						//System.out.println("read fun -> forSendingPacket -> " + forSendingPacket.peek().debugString());
						debugMessages.push(new String("Sent: \n"));
						debugMessages.push(forSendingPacket.peek().debugString());
						con.responseBuffer = ByteBuffer.wrap(forSendingPacket.pop().getByteData());
						Thread.sleep(1);
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 
    }

    private void write(SelectionKey key){
    	
        try {
			DatagramChannel chan = (DatagramChannel)key.channel();
			
			Connection con = (Connection)key.attachment();
			
			//System.out.println("write fun -> forSendingPacket -> " + forSendingPacket.peek().debugString());

			chan.send(con.responseBuffer, con.socketAddress);
			
			con.responseBuffer.clear();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }



}