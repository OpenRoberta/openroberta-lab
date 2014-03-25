package lejos.robotics.objectdetection;

/*
 * TODO: Testing:
 * 1. See how code looks to make a range sensor proportionally rotate left or right when doing an arc.
 * More of a move listener thing maybe, but needs to see the angle.
 * 2. Implement a bumper navigator and echo navigator sample.
 * 3. See how easy it is to react differently based on criteria (such as person ID - chase or run away depending on person). 
 * 4. Implement this API in PathPlanner/PathController. Will need setFeatureDetector() and alt constructor. 
 */

/**
 * <p>A FeatureDetector is capable of detecting objects and notifying listeners when it detects something. A Feature is
 * a term for any property that can be added to map data. The FeatureListener is notified when an object is detected,
 * even if it has previously detected the same object.</p>
 * 
 * <p>The most basic feature is the Range Feature, which indicates the position of the detected object relative
 * to the "center" of the robot (normally the point mid-way between the drive wheels). A robot with many bumpers
 * arrayed around the robot will also report the bumper location relative to the robot center.</p>
 * 
 * <p>There can be many different qualities recorded by different FeatureDetector implementations. For example, you could
 * implement a VectorFeatureDetector that could take multiple readings of an object, determine any change in position, 
 * and return the velocity/vector of the object (provided the sensor is capable of identifying an object and change in 
 * position). For example, a camera could note the change in position of objects and estimate the vector/velocity of 
 * the object.</p>
 * 
 * <p><i>Note: Because {@link FeatureListener#featureDetected(Feature, FeatureDetector)} and {@link FeatureDetector#scan()} are
 * only capable of returning a Feature object, any classes that want to read extended feature qualities (e.g.
 * vector, color, or person data) would need to use an instanceof test to see if it is the appropriate data container,
 * then cast the object into that type in order to retrieve the unique data.</i></p> 
 * 
 * @see lejos.robotics.objectdetection.FeatureListener
 * @author BB based on concepts by Lawrie Griffiths
 *
 */
public interface FeatureDetector {

	/**
	 * Adds a listener to the FeatureDetector. The FeatureListener will be notified when objects are detected. 
	 * 
	 * @param listener The FeatureListener that is notified every time a feature is detected.
	 * 
	 */
	public void addListener(FeatureListener listener);
	
	// TODO: Is null the best thing to return if it doesn't detect anything? 
	/**
	 * <p>Performs a single scan for an object and returns the results. If an object is not detected, this
	 * method returns <b>null</b>.</p>
	 * <p><i>Warning: Make sure to check for a null object before trying to read data from the returned 
	 * Feature object, otherwise your code will throw a null pointer exception.</i></p>  
	 * @return A feature it has detected. null if nothing found. 
	 */
	public Feature scan();
	
	/**
	 * Enable or disable detection of objects.
	 * 
	 * @param on true enables detection and notifications, false disables this class until it is enabled again.
	 */
	public void enableDetection(boolean on);
	
	/**
	 * Indicates if automatic scanning mode and listener notification is currently enabled. (true by default)
	 * @return true if enabled, false if not
	 */
	public boolean isEnabled();
	
	/**
	 * The minimum delay between notification of readings from the feature detector. If no objects are detected,
	 * no notification will occur. Some sensors, such as touch sensors, check the sensor more frequently than other
	 * sensors, such as range sensors.  
	 * 
	 * @return The delay between sensor readings. 
	 */
	public int getDelay();
	
	/**
	 * Sets the minimum delay between readings from the feature detector. The notification thread will notify 
	 * FeatureListener objects every <i>delay</i> milliseconds, unless it takes longer to retrieve readings
	 * from the sensor.  
	 * @param delay The FeatureDetector will return one new set of readings every <i>delay</i> milliseconds. 
	 */
	public void setDelay(int delay);
}
