package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class RoutingUpdateInitControl extends ControlMessage {
	
	//0001 0000
	private static final int routingFlag = 16;

	public RoutingUpdateInitControl(String Source, String Destination)
			throws UnsupportedEncodingException, Exception {
		super(Source, Destination, routingFlag);
		// TODO Auto-generated constructor stub
	}

}
