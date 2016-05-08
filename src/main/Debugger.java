package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import message.Message;
import message.control.ControlMessage;
import message.control.type.ACKControl;
import message.data.DataMessage;
import message.data.type.FileData;
import network.DatagramManager;

/**
 * 
 * @author ivo.pure
 *
 */
public class Debugger {

	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		// TODO Auto-generated method stub

		
		DatagramManager dm = new DatagramManager(1023);
		/**
		byte[] abyte = "Message 2".getBytes();
		String s = DatatypeConverter.printHexBinary(abyte);
		System.out.println(s);
		System.out.println(s.substring(0, 2));
		System.out.println(s.substring(2, 4));
		*/
		/**
		Message m3 = new ACKControl("asbc", "asad");
		System.out.println(m3.toString() + "\n");
		Message m4 = new Message(m3.getByteData());
		System.out.println(m4.toString());
		
		*/
		
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
