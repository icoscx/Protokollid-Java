package message.data.type;

import java.io.UnsupportedEncodingException;

import message.data.DataMessage;

public class SessionData extends DataMessage {
	
	//0001 0000
	//private static final int sessionFlag = 16;

	public SessionData(String Source, String Destination, int length, String payload, boolean sequenceNumber,
			boolean lastFragment) throws UnsupportedEncodingException, Exception {
		super(Source, Destination, fixSessionDataFlag(sequenceNumber, lastFragment), length, payload, sequenceNumber, lastFragment);
		// TODO Auto-generated constructor stub
	}

	public SessionData(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	

}
