package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class KeepAliveControl extends ControlMessage {

	public KeepAliveControl(String Source, String Destination, int Flag)
			throws UnsupportedEncodingException, Exception {
		super(Source, Destination, Flag);
		// TODO Auto-generated constructor stub
	}

}
