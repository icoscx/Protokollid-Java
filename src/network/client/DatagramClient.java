package network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

import message.Message;
import network.MessageParser;
import network.NetworkVars;


public class DatagramClient {
	
	public boolean hasFailed = false;
	
	private Message message;
	
	public String error = "";
	
	public Message send(Message msg){

		this.message = msg;
		
		try {
			InetAddress IPAddress = InetAddress.getByName(msg.getDestinationIP());
			byte[] sendData = new byte[msg.getMessageTotalLength()];
			sendData = msg.getByteData();
			if(msg.getMessageTotalLength() != sendData.length){
					throw new Exception("Calculated message data does not match real length of byte array to be sent");
			}
			System.out.println("\nSending: " + msg.toString() + "\n");
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, msg.getDestinationPort());
			//DatagramSocket clientSocket = new DatagramSocket(NetworkVars.sendingPort);
			DatagramSocket clientSocket = new DatagramSocket();
			clientSocket.setSoTimeout(NetworkVars.socketTimeOut);
			clientSocket.send(sendPacket);
			clientSocket.close();
			
			/**
			
			//increased size for not crashing
			byte[] receiveData = new byte[100];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			//System.out.println(clientSocket.getLocalPort());
			clientSocket.close();
			
			//????????????????????????????????????
			if(receiveData.length < 13){
				throw new Exception("DatagramClient: Packet length shorter than 13 bytes");
			}
			this.message = new Message(receivePacket.getData());
			*/
			//not needed, reply should only be fixed size
			//MessageParser mp = new MessageParser();
			//message = mp.parser(message);
			
			//return message;
			
		} catch (Exception e) {
			// TODO: handle exception
			this.hasFailed = true;
			error = e.toString();
		}
		return message;

	      
	}
	

}
