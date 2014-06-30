package lejos.robotics;

/**
 * Represent a single range reading
 */
public class RangeReading {
	private float range, angle;
	
	/**
	 * Create the reading
	 * 
	 * @param angle the angle relative to the heading
	 * @param range the range reading
	 */
	public RangeReading(float angle, float range) {
		this.range = range;
		this.angle = angle;
	}
	
	/**
	 * Get the range reading
	 * 
	 * @return the range reading
	 */
	public float getRange() {
		return range;
	}
	
	/**
	 * Get the angle of the range reading
	 * 
	 * @return the angle relative to the robot heading
	 */
	public float getAngle() {
		return angle;
	}
	
	/**
	 * Test if reading is invalid
	 * 
	 * @return true iff the reading is invalid
	 */
	public boolean invalidReading() {
		return range < 0;
	}
}
