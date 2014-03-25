package lejos.hardware.device;

/**
 * Interface for dLights from Dexter Industries
 * @author Aswin Bouwmeester
 *
 */
public interface DLight {
	
	/**
	 * Enables the dLight. 
	 */
	public void enable();
	
	/**
	 * Disables the dLight.
	 * Values for color and blinking pattern are not overwritten and will 
	 * still be in effect after enabling the dLight again.
	 */
	public void disable();
	
	/** Queries the status of the dLight
	 * @return
	 * True, the dLight is enabled. False, the dLight is off.
	 */
	public boolean isEnabled();
	
	/**
	 * Changes the color of the LED according to specified RGB color.
	 * Each of the color values should be between 0 (fully off) and 255 (fully on).
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue);
	
	/**
	 * Changes the color of the LED according to specified RGB color.
	 * Each of the color values should be between 0 (fully off) and 255 (fully on).
	 * @param rgb
	 * Integer array containing RGB colors
	 */
	public void setColor(int[] rgb);
	
	/**
	 * Changes the color of the LED according to specified HSL color.
	 * @param hue
	 * The hue value in the range of 0-360
	 * @param saturation
	 * The saturation value in the range of 0-100
	 * @param luminosity
	 * The saturation luminosity value in the range of 0-100
	 */
	public void setHSLColor(int hue, int saturation, int luminosity);
	
	/**
	 * Specifies the blinking pattern of the LED. Blinnking mode should be enabled 
	 * for the pattern to be in effect.
	 * @param seconds
	 * The total time of a blinking cycle (in seconds)
	 * @param percentageOn
	 * The percentage of the time the LED is on within a blinking cycle
	 */
	public void setBlinkingPattern(float seconds, int percentageOn);
	
	/**
	 * Enables blinking pattern. The blinking pattern should be set with the SetBlinkingPattern method.
	 */
	public void enableBlinking();
	
	/**
	 * Disables blinking. The blinking pattern itself remains in memory. 
	 */
	public void disableBlinking();
	
	/**
	 * Queries the blinking mode of the dLight
	 * @return
	 * True is blinking is enabled. False if blinking is disabled.
	 */
	public boolean isBlinkingEnabled();
		
	/**
	 * Returns the RGB color of the LED
	 * Each of the color values is between 0 (fully off) and 255 (fully on).
		 * @param rgb
	 */
	public void getColor(int[] rgb);
	
	/**
	 * Sets the PWM value of the external LED driver of the dLight.
	 * Each dLight has two leads broken out that can be used to drive an external LED.
	 * @param value
	 * The values should be between 0 (fully off) and 255 (fully on).
	 */
	public void setExternalLED(int value);
	
	/**
	 * Gets the PWM value of the external LED driver of the dLight.
	 * Each dLight has two leads broken out that can be used to drive an external LED.
	 * @return 
	 * The return value is between 0 (fully off) and 255 (fully on).
	 */
	public int getExternalLED();
	

}
