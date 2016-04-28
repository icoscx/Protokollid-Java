package packet;

import javax.xml.bind.DatatypeConverter;

public class Message {
	
	private static final int maxExpectedUDPDatagram = 100;
	
	private byte[] receivedData;
	
	private String receivedDataHex;
	
	private String version;
	
	private String Source;
	
	private String Destination;
	
	private String Type;
	
	private String Flag;
	
	private String hopCount;
	
	private String length;
	
	private String payload;
	
	public Message(byte[] bytes) throws Exception{
		
		if(bytes.length > maxExpectedUDPDatagram){
			//logger here needed
			throw new Exception("Message received more data than expected >100");
			}
		receivedData = new byte[maxExpectedUDPDatagram];
		receivedData = bytes;
		receivedDataHex = createHexString(receivedData);
		splitData();
		
	}
	
	private void splitData(){
		
		version = receivedDataHex.substring(0, 2);
		Source = receivedDataHex.substring(2, 18);
		Destination = receivedDataHex.substring(18, 34);
		Type = receivedDataHex.substring(34, 36);
		Flag = receivedDataHex.substring(36, 38);
		hopCount = receivedDataHex.substring(38, 40);
		length = receivedDataHex.substring(40, 42);
		//21 bytes done, now the rest of it
		payload = receivedDataHex.substring(42);
		
	}
	
	private String createHexString(byte[] bytes){
		//in bigEndian
		return DatatypeConverter.printHexBinary(bytes);
	}

	public byte[] getReceivedData() {
		return receivedData;
	}

	public void setReceivedData(byte[] receivedData) {
		this.receivedData = receivedData;
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
		return receivedDataHex;
	}

	public void setReceivedDataHex(String receivedDataHex) {
		this.receivedDataHex = receivedDataHex;
	}
	
	
	
	
	
	

}
