package network.peering;

public class Route {
	
	private String destinationUuid = "";
	
	private String viaUuid = "";
	
	private int cost = 0;

	public Route(String destinationUuid, String viaUuid, int cost) {
		
		this.destinationUuid = destinationUuid;
		this.viaUuid = viaUuid;
		this.cost = cost;
		// TODO Auto-generated constructor stub
	}

	public String getDestinationUuid() {
		return destinationUuid;
	}

	public void setDestinationUuid(String destinationUuid) {
		this.destinationUuid = destinationUuid;
	}

	public String getViaUuid() {
		return viaUuid;
	}

	public void setViaUuid(String viaUuid) {
		this.viaUuid = viaUuid;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	

}
