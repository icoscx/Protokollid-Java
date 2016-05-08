package network;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class Connection {

	private static int receivingBufferSize = 100;
	
    public ByteBuffer requestBuffer;
    public ByteBuffer responseBuffer;
    public SocketAddress socketAddress;
    
    public Connection(){
    	
    	requestBuffer = ByteBuffer.allocate(receivingBufferSize);
    }
    
    public Connection(int responseBufferSize){
    	
    	requestBuffer = ByteBuffer.allocate(receivingBufferSize);
    	responseBuffer = ByteBuffer.allocate(responseBufferSize);
    }
	
	
}
