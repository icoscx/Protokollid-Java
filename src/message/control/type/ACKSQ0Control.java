package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class ACKSQ0Control extends ControlMessage {
	
	
	//0000 0100
	private static final int ACKFlag = 4;

	public ACKSQ0Control(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, ACKFlag);
		// TODO Auto-generated constructor stub
	}

	public ACKSQ0Control(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	
	
}
