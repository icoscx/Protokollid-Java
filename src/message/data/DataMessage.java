/**
 * 
 */
package message.data;

import java.io.UnsupportedEncodingException;

import message.Message;

/**
 * @author ivo.pure
 *
 */
public class DataMessage extends Message {
	
	//01 -> 0000 0001
	private static final int dataMessage = 1;


	/**
	 * 
	 * @param Source
	 * @param Destination
	 * @param Flag
	 * @param length
	 * @param payload
	 * @param sequenceNumber false = 0; true = 1;
	 * @param lastFragment false = 0; true = 1;
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public DataMessage(String Source, String Destination,int Flag, int length, String payload, boolean sequenceNumber, boolean lastFragment)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, dataMessage, Flag, length, payload);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param bytes
	 * @throws Exception
	 */
	/**
	public DataMessage(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	*/
	/**
	 * 
	 * @param sequenceNumber false; true;
	 * @param lastFragment false; true
	 * @return 0000 1001, 0000 1011, 0000 1000, 0000 1010
	 */
	protected static int fixFileDataMessageFlag(boolean sequenceNumber, boolean lastFragment){
		
		if(!sequenceNumber && lastFragment){
			//0000 1001
			//this.setFlag(Integer.toHexString(0x100 | 9).substring(1).toUpperCase());
			return 9;
		}else if(sequenceNumber && lastFragment){
			//0000 1011
			//this.setFlag(Integer.toHexString(0x100 | 11).substring(1).toUpperCase());
			return 11;
		}else if(!sequenceNumber && !lastFragment){
			//0000 1000
			//this.setFlag(Integer.toHexString(0x100 | 8).substring(1).toUpperCase());
			return 8;
		}else if(sequenceNumber && !lastFragment){
			//0000 1010
			//this.setFlag(Integer.toHexString(0x100 | 10).substring(1).toUpperCase());
			return 10;
		}
		
		return 8;
		
	}
	
	protected static int fixTextMessageFlag(boolean sequenceNumber, boolean lastFragment){
			
			if(!sequenceNumber && lastFragment){
				//0000 0101
				return 5;
			}else if(sequenceNumber && lastFragment){
				//0000 0111
				return 7;
			}else if(!sequenceNumber && !lastFragment){
				//0000 0100
				return 4;
			}else if(sequenceNumber && !lastFragment){
				//0000 0110
				return 6;
			}
			
			return 4;
			
	}
	
	protected static int fixSessionDataFlag(boolean sequenceNumber, boolean lastFragment){
		
		if(!sequenceNumber && lastFragment){
			//0001 0001
			return 17;
		}else if(sequenceNumber && lastFragment){
			//0001 0011
			return 19;
		}else if(!sequenceNumber && !lastFragment){
			//0001 0000
			return 16;
		}else if(sequenceNumber && !lastFragment){
			//0001 0010
			return 18;
		}
		
		return 16;
		
	}
	
	protected static int fixRoutingDataFlag(boolean sequenceNumber, boolean lastFragment){
		
		if(!sequenceNumber && lastFragment){
			//0010 0001
			return 33;
		}else if(sequenceNumber && lastFragment){
			//0010 0011
			return 35;
		}else if(!sequenceNumber && !lastFragment){
			//0010 0000
			return 32;
		}else if(sequenceNumber && !lastFragment){
			//0010 0010
			return 34;
		}
		
		return 32;
		
	}
	
	
	
	

}
