package message;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * 
 * @author ivo.pure
 *
 */
public class Message {
	
	private static final int maxExpectedUDPDatagram = 100;
	
	private static final int minExpectedUDPDatagram = 13;
	
	private static final int currentVersion = 1;
	
	private static final int defaultHopCount = 15;
	
	private byte[] byteData;
	
	private String dataHex = "";
	//in hex
	private String version = "";
	//in hex
	private String Source = "";
	//in hex
	private String Destination = "";
	//in hex
	private String Type = "";
	//in hex
	private String Flag = "";
	//in hex
	private String hopCount = "";
	//in hex
	private String length = "";
	//in hex
	private String payload = "";
	/**
	 * SOURCE AND DESTINATION ARE ALREADY IN HEX
	 * @param Source HEX string in length of 8
	 * @param Destination HEX string in length of 8
	 * @param Type integer
	 * @param Flag integer
	 * @param length integer
	 * @param payload normal string to be converted to HEX
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public Message(String Source,
			String Destination, int Type,
			int Flag,
			int length, String payload
			) throws UnsupportedEncodingException, Exception{
		
		if(Type > 255 || Flag > 255 || length > 255){
			throw new Exception("Maximum size for integer is 255 > FF");
		}
		
		if(!(Source.length() == 8) || !(Destination.length() == 8)){
			throw new Exception("String has to be 8 HEX characters");
		}
		
		if(payload.length() > 87){throw new Exception("Maximum size for paylaod is 87 characters");}
		
		this.version = Integer.toHexString(0x100 | currentVersion).substring(1).toUpperCase();
		//this.Source = createHexString(Source.getBytes("ASCII"));
		//this.Destination = createHexString(Destination.getBytes("ASCII"));
		this.Source = Source;
		this.Destination = Destination;
		this.Type = Integer.toHexString(0x100 | Type).substring(1).toUpperCase();
		this.Flag = Integer.toHexString(0x100 | Flag).substring(1).toUpperCase();
		this.hopCount = Integer.toHexString(0x100 | defaultHopCount).substring(1).toUpperCase();
		this.length = Integer.toHexString(0x100 | length).substring(1).toUpperCase();
		this.payload = createHexString(payload.getBytes("ASCII"));
		
		dataHex += this.version;
		dataHex += this.Source;
		dataHex += this.Destination;
		dataHex += this.Type;
		dataHex += this.Flag;
		dataHex += this.hopCount;
		dataHex += this.length;
		dataHex += this.payload;
		
		byteData = hexToBytes(dataHex);
		
	}
	
	public Message(byte[] bytes) throws Exception{
		
		if(bytes.length > maxExpectedUDPDatagram){
			//logger here needed
			throw new Exception("Message received more data than expected > " + maxExpectedUDPDatagram);
		}
		if(bytes.length < minExpectedUDPDatagram){
			//logger here needed
			throw new Exception("Message received less data than expected < " + minExpectedUDPDatagram);
		}
		byteData = trimBytes(bytes);
		dataHex = createHexString(byteData);
		splitData();
		
	}
	
	public byte[] trimBytes(byte[] bytes){
		
		int length = 0;
		String hexDataOfLength = "";
		
		hexDataOfLength = createHexString(bytes).substring(24, 26);
		length = Integer.decode("0x" + hexDataOfLength);
		
		return Arrays.copyOfRange(bytes, 0, 13 + length);
	}
	
	private void splitData(){
		
		version = dataHex.substring(0, 2);
		Source = dataHex.substring(2, 10);
		Destination = dataHex.substring(10, 18);
		Type = dataHex.substring(18, 20);
		Flag = dataHex.substring(20, 22);
		hopCount = dataHex.substring(22, 24);
		length = dataHex.substring(24, 26);
		//9 bytes done, now the rest of it
		payload = dataHex.substring(26);
		
		
	}
	
	private String createHexString(byte[] bytes){
		//in bigEndian
		return DatatypeConverter.printHexBinary(bytes);
	}
	
	private byte[] hexToBytes(String hexString) {
	     HexBinaryAdapter adapter = new HexBinaryAdapter();
	     byte[] bytes = adapter.unmarshal(hexString);
	     return bytes;
	}
	
	public byte[] getPayloadDataInBytes() throws Exception{
		
		return hexToBytes(this.payload);
	}
	
	public String getPayloadDataAscii() throws UnsupportedEncodingException{
		
		return new String(hexToBytes(this.payload), "ASCII");
	}
	
	public int getMessageTotalLength(){
		
		return dataHex.length()/2;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public String getDataHex() {
		return dataHex;
	}

	public void setDataHex(String dataHex) {
		this.dataHex = dataHex;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getHopCount() {
		return hopCount;
	}

	public void setHopCount(String hopCount) {
		this.hopCount = hopCount;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public static int getMaxexpectedudpdatagram() {
		return maxExpectedUDPDatagram;
	}

	public void printBinary(){
		for (byte b : byteData) {
			System.out.print(b);
		}
	}

	@Override
	public String toString() {
		//System.out.print("Binary: ");
		//printBinary();
		return "  \nMessage [ dataHex=" + dataHex + "\n, version=" + version
				+ ", Source=" + Source + ", Destination=" + Destination + ", Type=" + Type + ", Flag=" + Flag
				+ ", hopCount=" + hopCount + ", length=" + length + ", payload=" + payload + "] Tlength= " 
				+ getMessageTotalLength();
	}
	
	public String debugString(){
		
		 Date dNow = new Date( );
	     SimpleDateFormat ft = 
	     new SimpleDateFormat ("dd.MM HH:mm:ss:SSS");
		
		return "[" + ft.format(dNow) +"] version=" + version
				+ ", Source=" + Source + ", Destination=" + Destination + ", Type=" + Type + ", Flag=" + Flag
				+ ", hopCount=" + hopCount + ", length=" + length + ", payload=" + payload + "] Tlength=" 
				+ getMessageTotalLength() + " | " + this.getClass().getName();
	}
	//| dstip/port: " + getDestinationIP() + ":" + getDestinationPort() +
	//"| srcip/port: " + getSourceIP() + ":" + getSourcePort()
	
	
	
	
	
	

}
