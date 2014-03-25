package lejos.robotics;

import lejos.robotics.RangeReadings;

/**
 * Abstraction for a single range scanning sensor, rotating platform with a range finder,
 * or a complete robot, that obtains a set of range readings at a set of angles to#
 * the robot's heading.
 */
public interface RangeScanner {
	/**
	 * Take a set of range readings. The RangeReadings object defines the
	 * number of readings and their angles to the robot's heading.
	 * @return the range readings
	 */ 
	public RangeReadings getRangeValues();

    /**
     * Set the array of angles at which range readings are to be taken
     * @param angles
     */
     public void setAngles(float[] angles);
     
     /**
      * Return the range finder for use by other classes
      * @return the range finder
      */
     public RangeFinder getRangeFinder();
}
