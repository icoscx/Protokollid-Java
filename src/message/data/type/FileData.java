package message.data.type;

import java.io.UnsupportedEncodingException;

import message.data.DataMessage;

public class FileData extends DataMessage {
	
	//0000 1000
	private static final int fileDataFlag = 8;

	public FileData(String Source, String Destination, int length, String payload)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, fileDataFlag, length, payload);
		// TODO Auto-generated constructor stub
	}

}
