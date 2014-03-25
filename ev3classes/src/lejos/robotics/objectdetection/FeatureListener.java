package lejos.robotics.objectdetection;

/**
 * Any class implementing this interface and registering with a FeatureDetector will receive
 *  notifications when a feature is detected. 
 *  @see lejos.robotics.objectdetection.FeatureDetector#addListener(FeatureListener)
 *  @author BB based on concepts by Lawrie Griffiths
 *
 */
public interface FeatureListener {
	
	/**
	 * The angle and range (in a RangeReading) of a feature is reported when a feature is detected.
	 * @param feature The RangeReading, which contains angle and range.
	 */
	public void featureDetected(Feature feature, FeatureDetector detector);
	
}
