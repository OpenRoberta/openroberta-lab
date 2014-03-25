package lejos.hardware.device;

/**
 * Interface for infra-red transmitters that can send bytes to an RCX
 * 
 * @author Lawrie Griffiths
 */
public interface IRTransmitter {
	
	/**
	 * Send raw bytes to the RCX
	 * @param data the raw data
	 * @param len the number of bytes
	 */
	public void sendBytes(byte [] data, int len);
	
	/**
	 * Send a packet of data to the RCX
	 * @param packet
	 */
	public void sendPacket(byte[] packet);
	
	/**
	 * Send a remote control command to the RCX
	 * 
	 * @param msg the code for the remote command
	 */
	public void sendRemoteCommand(int msg);
	
	public void runProgram(int programNumber);
	
	public void forwardStep(int motor);
	
	public void backwardStep(int motor);
	
	public void beep();
	
	public void stopAllPrograms();
	
}
