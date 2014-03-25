package lejos.robotics;

/**
 * Abstraction for a Tachometer, which monitors speed of the encoder.
 *
 * @author BB
 *
 */
public interface Tachometer extends Encoder {
	
	
	  /**
	   * Returns the actual speed.
	   * 
	   * @return speed in degrees per second, negative value means motor is rotating backward
	   */
	  int getRotationSpeed();

}
