package message.data.type;

import java.io.UnsupportedEncodingException;

import message.data.DataMessage;

public class RoutingData extends DataMessage {

	//0010 0000
	private static final int routingFlag = 32;
	
	public RoutingData(String Source, String Destination, int length, String payload)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, routingFlag, length, payload);
		// TODO Auto-generated constructor stub
	}

}
