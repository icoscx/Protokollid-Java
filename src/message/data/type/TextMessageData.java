package message.data.type;

import java.io.UnsupportedEncodingException;

import message.data.DataMessage;

public class TextMessageData extends DataMessage {

	//0000 0100
	//private static final int textMessageFlag = 4;
	
	public TextMessageData(String Source, String Destination, int length, String payload, boolean sequenceNumber, boolean lastFragment)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, fixTextMessageFlag(sequenceNumber, lastFragment), length, payload, sequenceNumber, lastFragment);
		// TODO Auto-generated constructor stub
			//fixFlag(sequenceNumber, lastFragment);
	}

	public TextMessageData(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	
	

}
