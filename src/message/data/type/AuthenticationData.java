package message.data.type;

import java.io.UnsupportedEncodingException;

import message.data.DataMessage;

public class AuthenticationData extends DataMessage {

	//0001 0000
	private static final int authenticationFlag = 16;
	
	public AuthenticationData(String Source, String Destination, int length, String payload)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, authenticationFlag, length, payload);
		// TODO Auto-generated constructor stub
	}

}
