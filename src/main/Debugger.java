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
import message.control.type.ACKSQ0Control;
import message.data.DataMessage;
import message.data.type.FileData;
import message.data.type.TextMessageData;
import network.client.DatagramClient;
import network.server.DatagramServer;

/**
 * 
 * @author ivo.pure
 *
 */
public class Debugger {

	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		// TODO Auto-generated method stub

		String s = "QWWQWQWQWQWQW";
		Message m1 = new TextMessageData("aaaa", "xxxx", s.length() , s, false, true);
		m1.setDestinationIP("172.29.5.148");
		m1.setDestinationPort(9876);
		System.out.println("\nSending:" + m1.toString());
		DatagramClient dc = new DatagramClient();
		m1 = dc.send(m1);
		if(!dc.hasFailed){System.out.println("\nResponseReifned:"+m1.toString());}
		
		//DatagramManager dm = new DatagramManager(12344);
		
	}

}
