package network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import message.Message;
import network.NetworkVars;
import network.ParsingFunctions;


public class DatagramClient {

	public boolean send(Message msg){
		
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
			//clientSocket.close();
			//need answer, stop and wait
			byte[] receiveData = new byte[100];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			//System.out.println(clientSocket.getLocalPort());
			clientSocket.close();
			
			String sentType = msg.getType();
			String sentFlag = msg.getFlag();
			
			msg = ParsingFunctions.classifyPacket(new Message(receivePacket.getData()));
			
			if((sentType.equals("02") || sentType.equals("04")) 
					&& !msg.getFlag().equals("04")){
				throw new Exception("Wrong response, expecting ACK0 for Control and Auth messages");
			}else if(sentType.equals("01")){
				
				switch (sentFlag) {
				case value:
					
					break;
				case value:
					
					break;
				case value:
					
					break;
				case value:
					
					break;

				default:
					break;
				}
				
			}
			
			
			//else if(sentType.equals("01") && sentFlag.equals(""))
			//saan acki tagasi ja õige
			//if type data, need look for seq
			//any other type, expect ACK0
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;

	      
	}
	

}
