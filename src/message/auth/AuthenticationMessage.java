package message.auth;

import java.io.UnsupportedEncodingException;

import message.Message;

public class AuthenticationMessage extends Message {

	
	//0000 0010
	private static final int authMessage = 4;
	
	private static final int lengthOfData = 0;
	
	private static final String noPayload = "";
	
	public AuthenticationMessage(String Source, String Destination, int Flag)
			throws UnsupportedEncodingException, Exception {
		super(Source, Destination, authMessage, Flag, lengthOfData, noPayload);
		// TODO Auto-generated constructor stub
	}

	
	public AuthenticationMessage(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	

}
