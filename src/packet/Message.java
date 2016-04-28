package packet;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Message {
	
	private static final int maxExpectedUDPDatagram = 100;
	
	private byte[] byteData = null;
	
	private String dataHex = "";
	
	private String version = "";
	
	private String Source = "";
	
	private String Destination = "";
	
	private String Type = "";
	
	private String Flag = "";
	
	private String hopCount = "";
	
	private String length = "";
	
	private String payload = "";
	
	public Message(int version, String Source,
			String Destination, int Type,
			int Flag, int hopCount,
			int length, String payload
			) throws UnsupportedEncodingException, Exception{
		
		if(version > 255 || Type > 255 ||
				Flag > 255 || hopCount > 255 ||
				length > 255
				){
			throw new Exception("Maximum size for integer is 255 > FF");
		}
		
		if(Source.length() > 9 || Destination.length() > 9){
			throw new Exception("Maximum size for string is 8 characters");
		}
		
		if(payload.length() > 158){throw new Exception("Maximum size for paylaod is 79 characters");}
		
		this.version = Integer.toHexString(0x100 | version).substring(1);
		this.Source = createHexString(Source.getBytes("ASCII"));
		this.Destination = createHexString(Destination.getBytes("ASCII"));
		this.Type = Integer.toHexString(0x100 | Type).substring(1);
		this.Flag = Integer.toHexString(0x100 | Flag).substring(1);
		this.hopCount = Integer.toHexString(0x100 | hopCount).substring(1);
		this.length = Integer.toHexString(0x100 | length).substring(1);
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
			throw new Exception("Message received more data than expected >100");
			}
		byteData = new byte[maxExpectedUDPDatagram];
		byteData = bytes;
		dataHex = createHexString(byteData);
		splitData();
		
	}
	
	private void splitData(){
		
		version = dataHex.substring(0, 2);
		Source = dataHex.substring(2, 18);
		Destination = dataHex.substring(18, 34);
		Type = dataHex.substring(34, 36);
		Flag = dataHex.substring(36, 38);
		hopCount = dataHex.substring(38, 40);
		length = dataHex.substring(40, 42);
		//21 bytes done, now the rest of it
		payload = dataHex.substring(42);
		
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

	public byte[] getReceivedData() {
		return byteData;
	}

	public void setReceivedData(byte[] receivedData) {
		this.byteData = receivedData;
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

	public String getReceivedDataHex() {
		return dataHex;
	}

	public void setReceivedDataHex(String receivedDataHex) {
		this.dataHex = receivedDataHex;
	}

	@Override
	public String toString() {
		return "Message [byteData=" + createHexString(byteData) + ", dataHex=" + dataHex + ", version=" + version
				+ ", Source=" + Source + ", Destination=" + Destination + ", Type=" + Type + ", Flag=" + Flag
				+ ", hopCount=" + hopCount + ", length=" + length + ", payload=" + payload + "]";
	}
	
	
	
	
	
	

}
