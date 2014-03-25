package lejos.robotics;

/*
 * DEV NOTES: 
 * Raw = hardware values
 * Normalized = sign changed
 * Scaled = converted to % or some other scale
 * Calibrated = user calibrated values
 * TODO: If Detector interface made, move these Javadoc notes to there
 */

/**
 * A platform independent implementation for sensors that can detect white light levels.
 * @author BB
 *
 */
public interface LightDetector {
	
	/**
	 * Returns the calibrated and normalized brightness of the white light detected.
	 * @return A brightness value between 0 and 100%, with 0 = darkness and 100 = intense sunlight
	 */
	public int getLightValue();
	// TODO: In future, if we decide to include calibration methods in LightDetector interface,
	// the API docs for getLightValue() should include:
	// * Use calibrateLow() to set the zero level, and calibrateHigh to set the 100 level.

	// TODO: Names: getLightValue, getLevel, getLightLevel, getLight()
	
	
	/**
	 * Returns the normalized value of the brightness of the white light detected, such that
	 * the lowest value is darkness and the highest value is intense bright light.
	 * @return A raw value, between getLow() and getHigh(). Usually 
	 * between 0 and 1023 but can be anything depending on hardware. low values = dark, high values = bright 
	 */
	public int getNormalizedLightValue();
	
	/**
	 * The highest raw light value this sensor can return from intense bright light.
	 * @return the high value
	 */
	public int getHigh();

	/**
	 * The lowest raw light value this sensor can return in pitch black darkness.
	 * @return the low value
	 */
	public int getLow();
}
