package network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import message.Message;
import network.MessageParser;


public class DatagramClient {
	
	public boolean hasFailed = false;
	
	private Message message;
	
	public Message send(Message msg){

		try {
			InetAddress IPAddress = InetAddress.getByName(msg.getDestinationIP());
			byte[] sendData = new byte[msg.getMessageTotalLength()];
			sendData = msg.getByteData();
			if(msg.getMessageTotalLength() != sendData.length){
					throw new Exception("Calculated message data does not match real length of byte array to be sent");
			}
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, msg.getDestinationPort());
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.setSoTimeout(2000);
			clientSocket.send(sendPacket);
			//increased size for not crashing
			byte[] receiveData = new byte[100];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			
			clientSocket.close();
			this.message = new Message(receivePacket.getData());
			System.out.println("\nReceived message: " + message.toString());
			
			//not needed, reply should only be fixed size
			MessageParser mp = new MessageParser();
			message = mp.parser(message);
			
			return message;
			
		} catch (Exception e) {
			// TODO: handle exception
			this.hasFailed = true;
		}
		return msg;

	      
	}
	

}
