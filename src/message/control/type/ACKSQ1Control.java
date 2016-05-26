package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class ACKSQ1Control extends ControlMessage {
	
	
	//0000 0110
	private static final int ACKFlag = 6;

	public ACKSQ1Control(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, ACKFlag);
		// TODO Auto-generated constructor stub
	}

	public ACKSQ1Control(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	

}
