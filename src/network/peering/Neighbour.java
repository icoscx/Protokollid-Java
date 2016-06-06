package network.peering;

import com.didisoft.pgp.PGPKeyPair;
import message.auth.type.InitializeAuth;
import network.ParsingFunctions;
import network.client.DatagramClient;

public class Neighbour {

	private String UUID = "";
	
	private String dstIP = "";
	
	private int dstPort = 0;
	
	private long timer = System.currentTimeMillis(); 
	
	
	public Neighbour(String uuid, String ip, int port) throws Exception {
		
		this.UUID = checkIfUuidExists(uuid);
		this.dstIP = ip;
		this.dstPort = port;
		//NetworkCore.authSequenceIP = ip;
		//NetworkCore.authSequencePort = port;
		performAuthentication();
		// TODO Auto-generated constructor stub
	}
	
	
	private void performAuthentication() throws Exception{
		
		DatagramClient dc = new DatagramClient(this.dstIP, this.dstPort);
		dc.send(new InitializeAuth(ParsingFunctions.myUUID, this.UUID));
		//x is found via y, with a cost of z
		Router.routingTable.add(new Route(this.UUID, this.UUID, 16 - dc.hopCountFromResponse));
		
	}
	
	private String checkIfUuidExists(String uuid) throws Exception{
		
		PGPKeyPair key = new PGPKeyPair("keys/" + uuid.toUpperCase());
		
		String calculatedID = key.getKeyIDHex();
		
		if(!uuid.toUpperCase().equals(calculatedID.toUpperCase())){
			throw new Exception("PGP key calculation error, no match for entered key: " + key.getKeyIDHex() + " and calculated key: "+ calculatedID);
		}
		
		return uuid;

	}


	public String getUUID() {
		return UUID;
	}


	public void setUUID(String uUID) {
		UUID = uUID;
	}


	public String getIP() {
		return dstIP;
	}


	public void setIP(String iP) {
		dstIP = iP;
	}


	public int getPort() {
		return dstPort;
	}


	public void setPort(int port) {
		this.dstPort = port;
	}


	public long getTimer() {
		return timer;
	}


	public void setTimer(long timer) {
		this.timer = timer;
	}
	
	

}
