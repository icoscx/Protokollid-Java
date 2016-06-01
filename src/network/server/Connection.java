package network.server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class Connection {

	private static int receivingBufferSize = 100;
	
    public volatile ByteBuffer requestBuffer;
    public volatile ByteBuffer responseBuffer;
    public volatile SocketAddress socketAddress;
    
	//private volatile Queue<Message> receivedPacket = new ConcurrentLinkedQueue<Message>();
	
	//private volatile Queue<Message> forSendingPacket = new ConcurrentLinkedQueue<Message>();
    
    public Connection(){
    	
    	requestBuffer = ByteBuffer.allocate(receivingBufferSize);
    	responseBuffer = ByteBuffer.allocate(receivingBufferSize);
    }

}
