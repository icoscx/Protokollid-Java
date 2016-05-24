package network;

import java.io.UnsupportedEncodingException;

import message.Message;
import message.auth.type.InitializeAuth;
import message.auth.type.SuccessAuth;

public class ParsingFunctions {
	
	
	public static Message classifyPacket(Message msg){
		
		switch (msg.getType()) {
		case "04":
			
			if(msg.getFlag().equals("01")){
				InitializeAuth ia = (InitializeAuth) msg;
				return ia;
			}
			
			if(msg.getFlag().equals("02")){
				SuccessAuth sa = (SuccessAuth) msg;
				return sa;
			}
			break;
			
		case "02":

		default:
			break;
		}
		
		
		return msg;
	}
	
	/**
	 * Remove 00
	 * @param msg Message to trim
	 * @return trimmed message, real leangth
	 */
	public static Message parser(Message msg){
		
		int length = Integer.decode("0x" + msg.getLength());
		//siai if juurde
		if(length > 13){
			msg.setDataHex(msg.getDataHex().substring(0, 26 + length*2));
			msg.setPayload(msg.getPayload().substring(0, length*2));
			}

		return msg;
	}

}
