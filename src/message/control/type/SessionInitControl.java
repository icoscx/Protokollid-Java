package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class SessionInitControl extends ControlMessage {
	
	//0100 0000
	private static final int sessionInitFlag = 64;

	public SessionInitControl(String Source, String Destination)
			throws UnsupportedEncodingException, Exception {
		
		
		super(Source, Destination, sessionInitFlag);
		// TODO Auto-generated constructor stub
	}

	public SessionInitControl(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	

}
