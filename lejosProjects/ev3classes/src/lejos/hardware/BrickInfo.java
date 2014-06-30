package lejos.hardware;

public class BrickInfo {
	private String name;
	private String ipAddress;
	private String type;
	
	public BrickInfo(String name, String ipAddress, String type) {
		this.name = name;
		this.ipAddress = ipAddress;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIPAddress() {
		return ipAddress;
	}
	
	public String getType() {
		return type;
	}
}
