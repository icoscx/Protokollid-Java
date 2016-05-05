package message.auth.type;

import java.io.UnsupportedEncodingException;

import message.auth.AuthenticationMessage;

public class InitializeAuth extends AuthenticationMessage {
	
	//0000 0001
	private static final int initializeFlag = 1;

	public InitializeAuth(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, initializeFlag);
		// TODO Auto-generated constructor stub
	}

}
