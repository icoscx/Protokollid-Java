package network;

import main.MainWindow;
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
	//set from main class
	public static String myUUID = "";
	
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
					return new InitializeAuth(msg.getByteData());
				}
				
				if(msg.getFlag().equals("02")){
					return new SuccessAuth(msg.getByteData());
				}
				
				break;
				
			case "02":
				
				if(msg.getFlag().equals("04")){
					return new ACKSQ0Control(msg.getByteData());

				}
				
				if(msg.getFlag().equals("06")){
					return new ACKSQ1Control(msg.getByteData());
				}
				
				if(msg.getFlag().equals("20")){
					return new FileTrasnferInitControl(msg.getByteData());
				}
				if(msg.getFlag().equals("08")){
					return new KeepAliveControl(msg.getByteData());
				}
				if(msg.getFlag().equals("10")){
					return new RoutingUpdateInitControl(msg.getByteData());
				}
				
				if(msg.getFlag().equals("01")){
					return new RSTControl(msg.getByteData());

				}
				if(msg.getFlag().equals("40")){
					return new SessionInitControl(msg.getByteData());
				}
				
				break;
				
			case "01":
				
				if(msg.getFlag().equals("08")
				|| msg.getFlag().equals("09")
				|| msg.getFlag().equals("0A")
				|| msg.getFlag().equals("0B")
						){
					return new FileData(msg.getByteData());
				}
				
				if(msg.getFlag().equals("20")
				|| msg.getFlag().equals("21")
				|| msg.getFlag().equals("22")
				|| msg.getFlag().equals("23")
						){
					return new RoutingData(msg.getByteData());
				}
				
				if(msg.getFlag().equals("04")
				|| msg.getFlag().equals("05")
				|| msg.getFlag().equals("06")
				|| msg.getFlag().equals("07")
						){
					return new TextMessageData(msg.getByteData());
				}
				
				if(msg.getFlag().equals("10")
				|| msg.getFlag().equals("11")
				|| msg.getFlag().equals("12")
				|| msg.getFlag().equals("13")
						){
					return new SessionData(msg.getByteData());
				}
				
				break;
				

			default:
				throw new Exception("ParsingFunctions: Failed to classify packet");
				//break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
		}
		
		return null;
	}
	
	public static Message packetFlow(Message msg) {
		// TODO Auto-generated method stub
    	
    	//if dst != me, relay();
    	
    	//volitaile: t1 r/w t2 r 
    	
		try {
			
			//String senderUUID = msg.getSource();
			
			switch (msg.getType()) {
			//auth
			case "04":
				
				if(msg.getFlag().equals("01")){
					return new InitializeAuth(msg.getByteData());
					
				}
				
				if(msg.getFlag().equals("02")){
					return new SuccessAuth(msg.getByteData());
				}
				
				break;
			//control
			case "02":
				
				if(msg.getFlag().equals("04")){
					return new ACKSQ0Control(msg.getByteData());
				}
				
				if(msg.getFlag().equals("06")){
					return new ACKSQ1Control(msg.getByteData());
				}
				
				if(msg.getFlag().equals("20")){
					return new FileTrasnferInitControl(msg.getByteData());
				}
				if(msg.getFlag().equals("08")){
					return new KeepAliveControl(msg.getByteData());
				}
				if(msg.getFlag().equals("10")){
					return new RoutingUpdateInitControl(msg.getByteData());
				}
				
				if(msg.getFlag().equals("01")){
					return new RSTControl(msg.getByteData());
				}
				if(msg.getFlag().equals("40")){
					return new SessionInitControl(msg.getByteData());
				}
				
				break;
			//data	
			case "01":
				
				if(msg.getFlag().equals("08") //1000
				|| msg.getFlag().equals("09") //1001
				|| msg.getFlag().equals("0A") //1010
				|| msg.getFlag().equals("0B") //1011
						){
					return new FileData(msg.getByteData());
				}
				
				if(msg.getFlag().equals("20")//0010 0000
				|| msg.getFlag().equals("21")//0010 0001
				|| msg.getFlag().equals("22")//0010 0010
				|| msg.getFlag().equals("23")//0010 0011
						){
					return new RoutingData(msg.getByteData());
				}
				
				if(msg.getFlag().equals("04") //0100
				|| msg.getFlag().equals("05") //0101
				|| msg.getFlag().equals("06") //0110
				|| msg.getFlag().equals("07") //0111
						){
					//TextMessageData ia = new TextMessageData(msg.getByteData());

					if(msg.getFlag().equals("04") || msg.getFlag().equals("05")){
						String s = msg.getSource();
						msg = new ACKSQ0Control(myUUID, "AAAA");
						msg.setDestination(s);
					}else if(msg.getFlag().equals("06") || msg.getFlag().equals("07")){
						String s = msg.getSource();
						msg = new ACKSQ1Control(myUUID, "AAAA");
						msg.setDestination(s);
					}
					
					return msg;
				}
				
				if(msg.getFlag().equals("10") //0001 0000
				|| msg.getFlag().equals("11") //0001 0001
				|| msg.getFlag().equals("12") //0001 0010
				|| msg.getFlag().equals("13") //0001 0011
						){
					return new SessionData(msg.getByteData());
				}
				
				break;
				

			default:
				throw new Exception("ParsingFunctions: Packetflow error");
				//break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MainWindow.throwQueue.add(e.toString());
		}
    	//never arrives here
		return null;
	}


}
