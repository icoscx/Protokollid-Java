package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.xml.bind.DatatypeConverter;

import packet.Message;

public class Debugger {

	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		// TODO Auto-generated method stub

		/**
		byte[] abyte = "Message 2".getBytes();
		String s = DatatypeConverter.printHexBinary(abyte);
		System.out.println(s);
		System.out.println(s.substring(0, 2));
		System.out.println(s.substring(2, 4));
		*/
		
		//System.out.println(Integer.toHexString(0x100 | 13).substring(1));
		Message m1 = new Message(1, "abcdefgh", "ijklmnop", 100, 45, 11, 50, "abclolsnaiper");
		System.out.println(m1.toString() + "\n");
		Message m2Receivedm1 = new Message(m1.getByteData());
		System.out.println(m2Receivedm1.toString() + "\n");
		
		/**
		  //while(true){
		      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		      DatagramSocket clientSocket = new DatagramSocket();
		      InetAddress IPAddress = InetAddress.getByName("172.29.5.141");  
		      //String sentence = inFromUser.readLine();
		      //byte[] sendData = new byte[sentence.length()];
		      byte[] sendData = m1.getByteData();
		      //sendData = sentence.getBytes();
		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		      clientSocket.send(sendPacket);
		      
		      byte[] receiveData = new byte[100];
		      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		      clientSocket.receive(receivePacket);
		      String modifiedSentence = new String(receivePacket.getData());
		      System.out.println("FROM SERVER:" + modifiedSentence);
		      clientSocket.close();
		  //}
		   * 
		   */
		
	}

}
