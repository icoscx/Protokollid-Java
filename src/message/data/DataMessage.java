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
	
	protected static int fixFlag(boolean sequenceNumber, boolean lastFragment){
		
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
	
	

}
