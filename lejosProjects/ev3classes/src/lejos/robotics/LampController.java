package lejos.robotics;

/**
 * Interface for a light sensor that also includes a lamp (usually LED) to provide illumination.
 * @author BB
 *
 */
public interface LampController{
// TODO: Possible names: LEDLightDetector (too specific), FloodlightDetector, FloodLightDetector, FloodlightLightDetector. 
	
	/**
	 * Turns the default LED light on or off. If the sensor has more than one lamp color, this will
	 * control the red LED.
	 * 
	 * @param floodlight true to turn on lamp, false for off (ambient light only).
	 */
	public void setFloodlight(boolean floodlight);
	
	/**
	 * Checks if the floodlight is currently on. 
	 * @return true if on, false if off.
	 */
	public boolean isFloodlightOn();
	
	/**
	 * Returns the color of the floodlight, including Color.NONE.
	 * @return An enumeration of the current color.
	 */
	public int getFloodlight();
	
	/**
	 * Used to turn on or off the floodlight by color. If the sensor has multiple light colors, you can control
	 * which color is turned on or off. If the color does not exist, it does nothing and returns false. You can turn
	 * the floodlight off by using Color.NONE.
	 * @param color Use {@link lejos.robotics.Color} enumeration constant to control lamp colors.
	 * @return True if lamp changed, false if lamp color doesn't exist for this sensor. 
	 */
	 
	public boolean setFloodlight(int color);
	
}
