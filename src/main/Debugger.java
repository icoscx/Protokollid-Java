package main;

import java.io.UnsupportedEncodingException;

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
		Message m1 = new Message(1, "abcdefgh", "ijklmnop", 100, 45, 10, 50, "abclolsnaiper");
		System.out.println(m1.toString());
	}

}
