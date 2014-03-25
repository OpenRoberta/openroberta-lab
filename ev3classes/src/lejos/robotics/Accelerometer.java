package lejos.robotics;

/**
 * Interface for Acceleration sensors
 * 
 * @author Lawrie Griffiths
 *
 */
public interface Accelerometer {	
	/**
	 * Measures the x-axis of the accelerometer, in meters/second^2.
	 * @return acceleration in m/s^2
	 */
	public int getXAccel();
	
	/**
	 * Measures the y-axis of the accelerometer, in meters/second^2.
	 * @return acceleration in m/s^2
	 */
	public int getYAccel();
	/**
	 * Measures the z-axis of the accelerometer, in meters/second^2.
	 * @return acceleration in m/s^2
	 */
	public int getZAccel();
}
