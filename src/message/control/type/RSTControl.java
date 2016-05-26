package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class RSTControl extends ControlMessage {
	
	//0000 0001
	private static final int RSTFlag = 1;

	public RSTControl(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, RSTFlag);
		// TODO Auto-generated constructor stub
	}

	public RSTControl(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	

}
