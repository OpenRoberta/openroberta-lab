package lejos.remote.nxt;

/**
 * Container for holding the output state values.
 * 
 * @author <a href="mailto:bbagnall@mts.net">Brian Bagnall</a>
 * @version 0.2 September 9, 2006
 * @see NXTCommand
 * 
 */
public class OutputState {
	public byte status; // Status of the NXTCommand.getOutputState command.
	public int outputPort; // (Range: 0 to 2)
	public byte powerSetpoint; // -100 to 100
	public int mode; //(bit-field) // see NXTProtocol for enumeration
	public int regulationMode; // see NXTProtocol for enumeration
	public byte turnRatio; // -100 to 100
	public int runState; // see NXTProtocol for enumeration
	public long tachoLimit; // Current limit on a movement in progress, if any
	public int tachoCount; // Internal count. Number of counts since last reset of the motor counter)
	public int blockTachoCount; // Current position relative to last programmed movement
	public int rotationCount; // Current position relative to last reset of the rotation sensor for this motor)

	public OutputState(int port) {
		outputPort = port;
	}
}