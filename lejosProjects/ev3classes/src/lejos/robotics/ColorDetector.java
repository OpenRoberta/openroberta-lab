package lejos.robotics;

/**
 * This interface defines the methods of a generic ColorDetector object.
 * 
 */
public interface ColorDetector extends ColorIdentifier {

	/**
	 * Return the Red, Green and Blue values together in one object.
	 * @return Color object containing the three RGB component values between 0-255.
	 */
	public Color getColor();
	
}
