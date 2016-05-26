package network;

import message.Message;
import message.auth.type.InitializeAuth;
import message.auth.type.SuccessAuth;
import message.control.type.ACKSQ0Control;
import message.control.type.ACKSQ1Control;
import message.control.type.FileTrasnferInitControl;
import message.control.type.KeepAliveControl;
import message.control.type.RSTControl;
import message.control.type.RoutingUpdateInitControl;
import message.control.type.SessionInitControl;
import message.data.type.FileData;
import message.data.type.RoutingData;
import message.data.type.SessionData;
import message.data.type.TextMessageData;

public class ParsingFunctions {
	
	/**
	 * Cast incoming messages to correct type
	 * @param msg to cast
	 * @return Casted message
	 */
	public static Message classifyPacket(Message msg){
		
		try {
			switch (msg.getType()) {
			
			case "04":
				
				if(msg.getFlag().equals("01")){
					InitializeAuth ia = new InitializeAuth(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("02")){
					SuccessAuth sa = new SuccessAuth(msg.getByteData());
					return sa;
				}
				
				break;
				
			case "02":
				
				if(msg.getFlag().equals("04")){
					ACKSQ0Control ia = new ACKSQ0Control(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("06")){
					ACKSQ1Control sa = new ACKSQ1Control(msg.getByteData());
					return sa;
				}
				
				if(msg.getFlag().equals("20")){
					FileTrasnferInitControl sa = new FileTrasnferInitControl(msg.getByteData());
					return sa;
				}
				if(msg.getFlag().equals("08")){
					KeepAliveControl sa = new KeepAliveControl(msg.getByteData());
					return sa;
				}
				if(msg.getFlag().equals("10")){
					RoutingUpdateInitControl sa = new RoutingUpdateInitControl(msg.getByteData());
					return sa;
				}
				
				if(msg.getFlag().equals("01")){
					RSTControl sa = new RSTControl(msg.getByteData());
					return sa;
				}
				if(msg.getFlag().equals("40")){
					SessionInitControl sa = new SessionInitControl(msg.getByteData());
					return sa;
				}
				
				break;
				
			case "01":
				
				if(msg.getFlag().equals("08")
				|| msg.getFlag().equals("09")
				|| msg.getFlag().equals("0A")
				|| msg.getFlag().equals("0B")
						){
					FileData ia = new FileData(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("20")
				|| msg.getFlag().equals("21")
				|| msg.getFlag().equals("22")
				|| msg.getFlag().equals("23")
						){
					RoutingData ia = new RoutingData(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("04")
				|| msg.getFlag().equals("05")
				|| msg.getFlag().equals("06")
				|| msg.getFlag().equals("07")
						){
					TextMessageData ia = new TextMessageData(msg.getByteData());
					return ia;
				}
				
				if(msg.getFlag().equals("10")
				|| msg.getFlag().equals("11")
				|| msg.getFlag().equals("12")
				|| msg.getFlag().equals("13")
						){
					SessionData ia = new SessionData(msg.getByteData());
					return ia;
				}
				
				break;
				

			default:
				throw new Exception("ParsingFunctions: Failed to classify packet");
				//break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
