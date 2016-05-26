package network.server;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import message.Message;

public class Connection {

	private static int receivingBufferSize = 100;
	
    public volatile ByteBuffer requestBuffer;
    public volatile ByteBuffer responseBuffer;
    public volatile SocketAddress socketAddress;
    
	private volatile Queue<Message> receivedPacket = new LinkedList<Message>();
	
	private volatile Queue<Message> forSendingPacket = new LinkedList<Message>();
    
    public Connection(){
    	
    	requestBuffer = ByteBuffer.allocate(receivingBufferSize);
    	responseBuffer = ByteBuffer.allocate(receivingBufferSize);
    }
    
    public Connection(int responseBufferSize){
    	
    	requestBuffer = ByteBuffer.allocate(receivingBufferSize);
    	responseBuffer = ByteBuffer.allocate(responseBufferSize);
    }
    
    public synchronized void setResponseBufferSize(int i){
    	
    	this.responseBuffer = ByteBuffer.allocate(i);
    }
    
	public synchronized Message getReceivedPacket() {
		return this.receivedPacket.poll();
	}
	
	public synchronized Message getReadOnlyReceivedPacket() {
		return this.receivedPacket.peek();
	}

	public synchronized void setReceivedPacket(Message receivedPacket) {
		this.receivedPacket.add(receivedPacket);
	}

	public synchronized Message getForSendingPacket() {
		return this.getForSendingPacketStack().poll();
	}
	
	public synchronized Message getReadOnlyForSendingPacket() {
		return this.getForSendingPacketStack().peek();
	}

	public synchronized void setForSendingPacket(Message forSendingPacket) {
		this.getForSendingPacketStack().add(forSendingPacket);
	}

	/**
	 * @return the forSendingPacket
	 */
	public synchronized Queue<Message> getForSendingPacketStack() {
		return forSendingPacket;
	}

	/**
	 * @param forSendingPacket the forSendingPacket to set
	 */
	public synchronized void setForSendingPacketStack(Queue<Message> forSendingPacket) {
		this.forSendingPacket = forSendingPacket;
	}
	
	
}
