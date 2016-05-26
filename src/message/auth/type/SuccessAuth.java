package message.auth.type;

import java.io.UnsupportedEncodingException;

import message.auth.AuthenticationMessage;

public class SuccessAuth extends AuthenticationMessage {
	
	//0000 0010
	private static final int authSuccessFlag = 2;

	public SuccessAuth(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, authSuccessFlag);
		// TODO Auto-generated constructor stub
	}

	public SuccessAuth(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	

}
