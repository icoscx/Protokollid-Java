package message.data.type;

import java.io.UnsupportedEncodingException;

import message.data.DataMessage;

public class TextMessageData extends DataMessage {

	//0000 0100
	private static final int textMessageFlag = 4;
	
	public TextMessageData(String Source, String Destination, int length, String payload)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, textMessageFlag, length, payload);
		// TODO Auto-generated constructor stub
	}

}
