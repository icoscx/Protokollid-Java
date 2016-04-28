package main;

import javax.xml.bind.DatatypeConverter;

public class Debugger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		byte[] abyte = "Message 2".getBytes();
		
		String s = DatatypeConverter.printHexBinary(abyte);
		
		System.out.println(s);
		
		System.out.println(s.substring(0, 2));
		System.out.println(s.substring(2, 4));
		
	}

}
