package message.control.type;

import java.io.UnsupportedEncodingException;

import message.control.ControlMessage;

public class FileTrasnferInitControl extends ControlMessage {
	
	//0010 0000
	private static final int fileTransFlag = 32;

	public FileTrasnferInitControl(String Source, String Destination)
			throws UnsupportedEncodingException, Exception {
		super(Source, Destination, fileTransFlag);
		// TODO Auto-generated constructor stub
	}

}
