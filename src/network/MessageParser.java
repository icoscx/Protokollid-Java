package network;

import message.Message;

public class MessageParser {
	
	
	public Message parser(Message msg){
		
		int length = Integer.decode("0x" + msg.getLength());
		//siai if juurde
		msg.setDataHex(msg.getDataHex().substring(0, 26 + length*2));
		msg.setPayload(msg.getPayload().substring(0, length*2));
		
		return msg;
	}

}
