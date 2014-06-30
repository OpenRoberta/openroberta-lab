package lejos.remote.nxt;

/**
 * Sensor input values for a remote NXT accessed via LCP.
 * 
 * @author <a href="mailto:bbagnall@mts.net">Brian Bagnall</a>
 * 
 */
public class InputValues {
	public int inputPort;
	/**
	 * NXT indicates if it thinks the data is valid
	 */
	public boolean valid = true;
	public boolean isCalibrated;
	public int sensorType;
	public int sensorMode;
	/**
	 * The raw value from the Analog to Digital (AD) converter.
	 */
	public int rawADValue;
	/**
	 * The normalized value from the Analog to Digital (AD) converter. I really don't
	 * know for sure which values are normalized yet.
	 * 0 to 1023
	 */
	public int normalizedADValue;
	/**
	 * The scaled value starts working after the first call to the sensor.
	 * The first value will be the raw value, but after that it produces scaled values.
	 * With the touch sensor, off scales to 0 and on scales to 1.
	 */
	public short scaledValue;
	/**
	 * Currently unused.
	 */
	public short calibratedValue;
}
