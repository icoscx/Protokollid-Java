package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class SequenceControl extends ControlMessage {
	
	//0000 0010
	private static final int sequenceFlag = 2;

	public SequenceControl(String Source, String Destination) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, sequenceFlag);
		// TODO Auto-generated constructor stub
	}

}
