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
import message.Message;
import message.auth.type.InitializeAuth;
import message.auth.type.SuccessAuth;
import message.control.type.ACKSQ0Control;
import message.control.type.ACKSQ1Control;
import message.control.type.FileTrasnferInitControl;
import message.control.type.KeepAliveControl;
import message.control.type.RSTControl;
import message.control.type.RoutingUpdateInitControl;
import message.control.type.SessionInitControl;
import message.data.type.FileData;
import message.data.type.RoutingData;
import message.data.type.SessionData;
import network.ParsingFunctions;


public class DatagramServer extends Thread{
	
	protected int port = 0;
	
	private Thread t;
	
	private String myUUID = "";
	
	public volatile Queue<Message> packetCache = new ConcurrentLinkedQueue<Message>();
	
	public volatile Queue<String> debugMessages = new ConcurrentLinkedQueue<String>();
	
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
                            	//System.out.println("*****Key readable, reading...*****");
                                read(key);
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                            	//System.out.println("*****Key writable, writing...*****");
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
		
					//System.out.println("\nReceived message: "+ aMessage.debugString());
					
					aMessage = ParsingFunctions.classifyPacket(aMessage);
					
					con.setReceivedPacket(aMessage);
					//receivedPacket.push(aMessage);
					
					//debugMessages.push(new String("Received: \n"));
					//debugMessages.push(con.getReadOnlyReceivedPacket().debugString());
					
					debugMessages.add(new String("Received: \n"));
					debugMessages.add(con.getReadOnlyReceivedPacket().debugString());
					

					con.requestBuffer.clear();

					//forSendingPacket.push(receivedPacket.pop());
					
					aMessage = packetFlow(aMessage);
					
					//con.setForSendingPacket(con.getReceivedPacket());
					con.setForSendingPacket(aMessage);

					while(!con.getForSendingPacketStack().isEmpty()){

						//debugMessages.push(new String("Sent: \n"));
						//debugMessages.push(con.getReadOnlyForSendingPacket().debugString());
						
						debugMessages.add(new String("Sent: \n"));
						debugMessages.add(con.getReadOnlyForSendingPacket().debugString());
						
						con.responseBuffer = ByteBuffer.wrap(con.getForSendingPacket().getByteData());
						////////////////
						//Thread.sleep(1);
						////////////////
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

    private Message packetFlow(Message msg) {
		// TODO Auto-generated method stub
    	
    	//if dst != me, relay();
    	
    	//volitaile: t1 r/w t2 r  //hashtable = sync hashmap = async
    	
		try {
			
			//String senderUUID = msg.getSource();
			
			switch (msg.getType()) {
			//auth
			case "04":
				
				if(msg.getFlag().equals("01")){
					InitializeAuth ia = new InitializeAuth(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("02")){
					SuccessAuth sa = new SuccessAuth(msg.getByteData());
					return sa;
				}
				
				break;
			//control
			case "02":
				
				if(msg.getFlag().equals("04")){
					ACKSQ0Control ia = new ACKSQ0Control(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("06")){
					ACKSQ1Control sa = new ACKSQ1Control(msg.getByteData());
					return sa;
				}
				
				if(msg.getFlag().equals("20")){
					FileTrasnferInitControl sa = new FileTrasnferInitControl(msg.getByteData());
					return sa;
				}
				if(msg.getFlag().equals("08")){
					KeepAliveControl sa = new KeepAliveControl(msg.getByteData());
					return sa;
				}
				if(msg.getFlag().equals("10")){
					RoutingUpdateInitControl sa = new RoutingUpdateInitControl(msg.getByteData());
					return sa;
				}
				
				if(msg.getFlag().equals("01")){
					RSTControl sa = new RSTControl(msg.getByteData());
					return sa;
				}
				if(msg.getFlag().equals("40")){
					SessionInitControl sa = new SessionInitControl(msg.getByteData());
					return sa;
				}
				
				break;
			//data	
			case "01":
				
				if(msg.getFlag().equals("08")
				|| msg.getFlag().equals("09")
				|| msg.getFlag().equals("0A")
				|| msg.getFlag().equals("0B")
						){
					FileData ia = new FileData(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("20")
				|| msg.getFlag().equals("21")
				|| msg.getFlag().equals("22")
				|| msg.getFlag().equals("23")
						){
					RoutingData ia = new RoutingData(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("04") //0100
				|| msg.getFlag().equals("05") //0101
				|| msg.getFlag().equals("06") //0110
				|| msg.getFlag().equals("07") //0111
						){
					//TextMessageData ia = new TextMessageData(msg.getByteData());
					
					packetCache.add(msg);

					if(msg.getFlag().equals("04") || msg.getFlag().equals("05")){
						msg = new ACKSQ0Control(myUUID, msg.getSource());
					}else if(msg.getFlag().equals("06") || msg.getFlag().equals("07")){
						msg = new ACKSQ1Control(myUUID, msg.getSource());
					}
					
					return msg;
				}
				
				if(msg.getFlag().equals("10")
				|| msg.getFlag().equals("11")
				|| msg.getFlag().equals("12")
				|| msg.getFlag().equals("13")
						){
					SessionData ia = new SessionData(msg.getByteData());
					return ia;
				}
				
				break;
				

			default:
				throw new Exception("ParsingFunctions: Failed to classify packet");
				//break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return null;
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

	public String getMyUUID() {
		return myUUID;
	}

	public void setMyUUID(String myUUID) {
		this.myUUID = myUUID;
	}
	
	
	
	


    
    



}