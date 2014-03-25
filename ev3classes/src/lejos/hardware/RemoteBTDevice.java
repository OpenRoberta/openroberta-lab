package lejos.hardware;

import java.io.Serializable;

public class RemoteBTDevice implements Serializable {
	
	private static final long serialVersionUID = -1668354353670941450L;
	private String name;
	private byte[] address;
	private byte[] cod;
	
	public RemoteBTDevice(String name, byte[] address, byte[] cod) {
		this.name = name;
		this.address = address;
		this.cod = cod;
	}
	
	public String getName() {
		return name;
	}
	
	public byte[] getDeviceAddress() {
		return address;
	}
	
	public byte[] getDeviceClass() {
		return cod;
	}
	
	public String getAddress() {
		StringBuilder sb = new StringBuilder();
		for(int j=5;j>=0;j--) {
			String hex = Integer.toHexString(address[j] & 0xFF).toUpperCase();
			if (hex.length() == 1) sb.append('0');
			sb.append(hex);
			if (j>0) sb.append(':');
		}
		return sb.toString();
	}
	
	public void authenticate(String pin) {
		
	}
}
