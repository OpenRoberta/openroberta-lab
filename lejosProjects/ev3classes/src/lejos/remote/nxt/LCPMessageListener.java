package lejos.remote.nxt;

public interface LCPMessageListener {
	
	public void messageReceived(byte inBox, String message);
}
