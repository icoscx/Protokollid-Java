package network.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import message.Message;

public class DatagramClient {
	
	public DatagramClient(){
		
		
	}
	
	private void send(Message msg) throws Exception{
	
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName("172.29.5.35");
	      byte[] sendData = new byte[msg.getMessageTotalLength()];
	      sendData = msg.getByteData();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1023);
	      clientSocket.send(sendPacket);
	      
	}

}
