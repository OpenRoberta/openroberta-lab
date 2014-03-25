package lejos.hardware.port;

/**
 * An abstraction for a port that supports Analog/Digital sensors.
 * 
 * @author Lawrie Griffiths/Andy.
 * 
 */
public interface AnalogPort extends IOPort, BasicSensorPort {
    /**
     * return the voltage present on pin 6 of the sensor port
     * @return voltage reading
     */
	public float getPin6();
	
    /**
     * return the voltage present on pin 1 of the sensor port
     * @return voltage reading
     */
	public float getPin1();
	
	/**
	 * Return a series of results obtained from the sensor.
	 * This is currently only used for the NXT color sensor
	 * @param vals Place to store the values
	 * @param offset Offset into the above array to start storing
	 * @param length number of values to read
	 */
	public void getFloats(float [] vals, int offset, int length);
}
