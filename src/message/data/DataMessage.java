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
	
	private int sequenceNumber = 0;
	
	private int lastFragment = 0;

	/**
	 * @param Source
	 * @param Destination
	 * @param Flag
	 * @param hopCount
	 * @param length
	 * @param payload
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public DataMessage(String Source, String Destination,int Flag, int length, String payload)
			throws UnsupportedEncodingException, Exception {
		
		super(Source, Destination, dataMessage, Flag, length, payload);
		// TODO Auto-generated constructor stub
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getLastFragment() {
		return lastFragment;
	}

	public void setLastFragment(int lastFragment) {
		this.lastFragment = lastFragment;
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
	
	

}
