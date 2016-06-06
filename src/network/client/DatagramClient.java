package network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.MainWindow;
import message.Message;
import network.NetworkVars;
import network.ParsingFunctions;


public class DatagramClient {
	
	private String ip = "";
	private int port = 0;
	public int hopCountFromResponse = 0;
	
	public static volatile Queue<String> debugMessages = new ConcurrentLinkedQueue<String>();
	/**
	 * 
	 * @param IP to send to
	 * @param port to send to
	 */
	public DatagramClient(String IP, int port){
		this.port = port;
		this.ip = IP;
	}

	public boolean send(Message msg){
		
		try {
			InetAddress IPAddress = InetAddress.getByName(this.ip);
			byte[] sendData = new byte[msg.getMessageTotalLength()];
			sendData = msg.getByteData();
			if(msg.getMessageTotalLength() != sendData.length){
					throw new Exception("Calculated message data does not match real length of byte array to be sent");
			}
			debugMessages.add(new String("Client: Sending: "));
			debugMessages.add(msg.debugString());
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, this.port);
			//DatagramSocket clientSocket = new DatagramSocket(NetworkVars.sendingPort);
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.setSoTimeout(NetworkVars.socketTimeOut);
			clientSocket.send(sendPacket);
			//need answer, stop and wait
			byte[] receiveData = new byte[100]; 
			//dont know received packet size, so 100 bytes, trim at message constructor
			//if there is time, need to implement bytebuffer here
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			clientSocket.close();
			
			String sentType = msg.getType();
			String sentFlag = msg.getFlag();
			
			msg = ParsingFunctions.classifyPacket(new Message(receivePacket.getData()));
			
			hopCountFromResponse = Integer.decode("0x" + msg.getHopCount());
			
			//die if unexpected responses
			if((sentType.equals("02") || sentType.equals("04")) 
					&& !msg.getFlag().equals("04")){
				throw new Exception("Wrong response, expecting always ACK0 for Control and Auth messages");
			}else if(sentType.equals("01")){
				
				if((sentFlag.equals("10")
						|| sentFlag.equals("11")
						|| sentFlag.equals("04")
						|| sentFlag.equals("05")
						|| sentFlag.equals("20")
						|| sentFlag.equals("21")
						|| sentFlag.equals("08")
						|| sentFlag.equals("09")
						) && !msg.getFlag().equals("04")){
					throw new Exception("Wrong response, expecting ACK0 as response for Sequence 0 of data message sent");
				}else if((sentFlag.equals("12")
						|| sentFlag.equals("13")
						|| sentFlag.equals("06")
						|| sentFlag.equals("07")
						|| sentFlag.equals("22")
						|| sentFlag.equals("23")
						|| sentFlag.equals("0A")
						|| sentFlag.equals("0B")
						) && !msg.getFlag().equals("06")){
					throw new Exception("Wrong response, expecting ACK1 as response for Sequence 1 of data message sent");
				}
				
			}
			
			debugMessages.add(new String("Client: Received: "));
			debugMessages.add(msg.debugString());

			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
			return false;
		}
		
		return true;
    
	}
	

}
