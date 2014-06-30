
package lejos.robotics;

/**
 * Abstraction for touch sensors
 *
 * @author Andy
 *
 */
public interface Touch {
	/**
	 * Check if the sensor is pressed.
	 * @return <code>true</code> if sensor is pressed, <code>false</code> otherwise.
	 */
	public boolean isPressed();
}
