package lejos.robotics.objectdetection;

import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;

/**
 * A Feature is an interface for information retrieved about an object detected by
 * sensors. Feature is a map feature that is detected and reported by a FeatureDetector.
 * @author BB based on concepts by Lawrie Griffiths
 */
public interface Feature {

	// TODO: It's possible that a detector would detect the coordinate position of something, not 
	// really have the relative position to vehicle available.
	
	/**
	 * Returns the RangeReading for this particular detected feature. If the sensor is capable of detecting multiple
	 * objects, this method will return the closest object it detected.
	 * 
	 * @return RangeReading object containing angle and range.
	 */
	public RangeReading getRangeReading();

	/**
	 * Returns a set of RangeReadings for a number of detected objects. If the sensor is only capable of returning
	 * a single reading, or if only one object was detected, it will only contain one RangeReading in the set.
	 * 
	 * @return RangeReadings object containing a set of RangeReading objects.
	 */
	public RangeReadings getRangeReadings();
	
	/**
	 * The time-stamp is the recorded system time when the range reading was taken. This is generally
	 * recorded in the constructor of the Feature implementation. The time-stamp can help 
	 * identify the vector of the detected object.
	 * @return The system time (in milliseconds) when the reading was taken. 
	 */
	public long getTimeStamp();
}
