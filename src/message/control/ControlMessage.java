package message.control;

import java.io.UnsupportedEncodingException;

import message.Message;

public class ControlMessage extends Message {
	
	//0000 0010
	private static final int controlMessage = 2;
	
	private static final int lengthOfData = 0;
	
	private static final String noPayload = null;

	public ControlMessage(String Source, String Destination, int Flag) throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, controlMessage, Flag, lengthOfData, noPayload);
		// TODO Auto-generated constructor stub
	}

	/**
	public ControlMessage(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	*/

}
