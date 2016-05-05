package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class ACKControl extends ControlMessage {
	
	private static final int ACKFlag = 4;

	public ACKControl(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, ACKFlag);
		// TODO Auto-generated constructor stub
	}

}
