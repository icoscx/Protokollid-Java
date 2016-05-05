package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class KeepAliveControl extends ControlMessage {
	
	//0000 1000
	private static final int keepAliveFlag = 8;

	public KeepAliveControl(String Source, String Destination)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, keepAliveFlag);
		// TODO Auto-generated constructor stub
	}

}
