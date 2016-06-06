package network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.MainWindow;
import message.Message;
import network.ParsingFunctions;


public class DatagramServer extends Thread{
	
	protected int port = 0;
	
	private Thread t;
	
	public static volatile Queue<Message> packetCache = new ConcurrentLinkedQueue<Message>();
	
	public volatile Queue<String> debugMessages = new ConcurrentLinkedQueue<String>();
	
	/**
	 * Port to listen. If behind NAT, Port forwarding needs to be configured.
	 * @param ListenPort
	 */
	public DatagramServer(){}
	
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
                            	MainWindow.throwQueue.add("Key not valid in selector, selecting new");
                                continue;
                            }

                            if (key.isReadable()) {
                            	//System.out.println("*****Key readable, reading...*****");
                                read(key);
                                //if relay, continiue here
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                            	//System.out.println("*****Key writable, writing...*****");
                                write(key);
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        } catch (Exception e) {
                        	MainWindow.throwQueue.add("DatagramServer: key error... " +(e.getMessage()!=null?e.getMessage():""));
                            e.printStackTrace();
                            MainWindow.throwQueue.add(e.toString());
                        }
                    }
                    Thread.sleep(1);
                } catch (Exception e) {
                	MainWindow.throwQueue.add("DatagramServer: selector error... " +(e.getMessage()!=null?e.getMessage():""));
                    e.printStackTrace();
                    MainWindow.throwQueue.add(e.toString());
                }

            }//end while true
            
        } catch (IOException e) {
        	MainWindow.throwQueue.add("(FATAL) DatagramServer: network error: " + (e.getMessage()!=null?e.getMessage():""));
            e.printStackTrace();
            MainWindow.throwQueue.add(e.toString());
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
					throw new Exception("DatagramServer: Packet length shorter than 13 bytes");
					
				}else if(currentPosition > 100){
					
					con.requestBuffer.clear();
					throw new Exception("DatagramServer: Packet length larger than 100 bytes");
					
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
		
					aMessage = ParsingFunctions.classifyPacket(aMessage);
					
					debugMessages.add(new String("SRV: Received: "));
					debugMessages.add(aMessage.debugString());

					con.requestBuffer.clear();
					
					packetCache.add(aMessage);
					
					aMessage = ParsingFunctions.packetFlow(aMessage);
					
					debugMessages.add(new String("SRV: Sent: "));
					debugMessages.add(aMessage.debugString());
					
					con.responseBuffer = ByteBuffer.wrap(aMessage.getByteData());

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MainWindow.throwQueue.add(e.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MainWindow.throwQueue.add(e.toString());
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
			MainWindow.throwQueue.add(e.toString());
		}
        
    }

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	


    
    



}