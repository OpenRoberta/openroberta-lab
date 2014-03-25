package lejos.robotics.objectdetection;

import java.util.ArrayList;
import lejos.robotics.RangeReading;
import lejos.robotics.Touch;
import lejos.robotics.geometry.Point;

/**
 * This class allows a touch sensor to be used as a defacto range sensor by reporting the position of the touch sensor
 * to the object detection API. The touch sensor will only notify one time when it is pressed and will not be ready to
 * notify again until the touch sensor is released.
 * 
 * @author BB
 *
 */
public class TouchFeatureDetector extends FeatureDetectorAdapter {

	private Touch touch_sensor;
	//private int x_offset, y_offset;
	private float angle = 0;
	private float range = 0;
	private static final int DELAY = 50;
	
	private ArrayList<FeatureListener> listeners = null;
	
	/**
	 * Creates a touch detector in which the touch sensor is assumed to be situated in the center of
	 * the robot. This is fine in situations where the robot only needs to react to a bumper
	 * contact. See the alternate constructor if more than one bumper is located around the robot. 
	 * @param touchSensor The touch sensor bumper.
	 */
	public TouchFeatureDetector(Touch touchSensor) {
		this(touchSensor, 0, 0);
	}
	
	/**
	 * If you want the bumpers to report contact relative to the geometry of where they are placed on the robot,
	 * you can provide the x, y offsets of each bumper relative to the center of the robot (the center is
	 * the halfway point between the drive wheels). Most bumpers are planar, so generally you would use the center of
	 * the bumper as the contact point.
	 *  
	 * @param touchSensor The touch sensor bumper.
	 * @param xOffset The offset (in units e.g. cm) left or right of center. Right is positive, left is negative.
	 * @param yOffset The offset (in units e.g. cm) forward or back of center. Forward is positive, back is negative.
	 */
	public TouchFeatureDetector(Touch touchSensor, double xOffset, double yOffset) {
		// TODO: Probably better to accept distance and range value instead! Simpler, more consistent
		
		super(DELAY);
		this.touch_sensor = touchSensor;
		//this.x_offset = xOffset;
		//this.y_offset = yOffset;
		
		// Calculate angle a distance of bumper from center:
		Point robot_center = new Point(0, 0);
		Point bumper_p = new Point((float)xOffset, (float)yOffset);
		range = (float)robot_center.distance(xOffset, yOffset);
		angle = robot_center.angleTo(bumper_p) - 90;
	}

	public Feature scan() {
		RangeFeature rf = null;
		if(touch_sensor.isPressed()) {
			RangeReading rr = new RangeReading(angle, range);
			rf = new RangeFeature(rr);
		}
		return rf;
	}

	@Override
	protected void notifyListeners(Feature feature) {
		super.notifyListeners(feature);
		// Wait until bumper is released before continuing to prevent multiple notifications from same press:
		while(touch_sensor.isPressed());
	}
}
