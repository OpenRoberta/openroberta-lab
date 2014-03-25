package lejos.robotics.objectdetection;

/*
 * 
 * DEVELOPER NOTES: This is a little weird because if you extend it to include say facial recognition information
 * about the person it detected, with multiple facial detections the information need to be indexed separately 
 * from the RangeReading objects.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.Transmittable;
import lejos.robotics.navigation.Pose;

/**
 * <p>This class is a basic data container for information retrieved about an object detected by
 * sensors. The RangeFeature contains only the most basic range information of a detected object, which
 * is {@link RangeReading}. The RangeReading contains range and angle data <i>relative</i> to the robot.</p>
 * 
 * <p>It can be extended to include more information, such as color. For example, a ColorFeature would 
 * rely on a ColorFeatureDetector to supply color information and range. The ColorFeatureDetector could extend
 * FeatureDetector and accept a camera in the constructor to identify color of a detected object. This type of 
 * class would be useful to allow soccer robots to identify team-mates, the soccer ball, and the different goals.</p> 
 *
 * @author BB
 *
 */
public class RangeFeature implements Feature, Transmittable  {

	private RangeReading rr;
	private RangeReadings rrs;
	private long timeStamp;
	private Pose pose = new Pose(0,0,0);
	
	/**
	 * Creates a RangeFeature containing a single RangeReading. If the {@link RangeFeature#getRangeReadings()}
	 * method is subsequently called, it will return a RangeReadings set containing only one RangeReading (rr).
	 * @param rr The RangeReading.
	 */
	public RangeFeature(RangeReading rr) {
		timeStamp = System.currentTimeMillis();
		this.rr = rr;
	}
	
	/**
	 * Creates a RangeFeature containing multiple RangeReadings. The {@link RangeFeature#getRangeReading()} method
	 * will return the RangeReading with the smallest range.
	 *  
	 * @param rrs A (@link RangeReadings} object containing a set of {@link RangeReading} values.
	 */
	public RangeFeature(RangeReadings rrs) {
		// TODO: This assumes index 0 is closest range. Should really check for the one with the shortest range.
		// Might also want to make sure this is not empty.
		this(rrs.get(0));
		this.rrs = rrs;
	}
	
	/**
	 * Creates a RangeFeature containing multiple RangeReadings. The {@link RangeFeature#getRangeReading()} method
	 * will return the RangeReading with the smallest range.
	 *  
	 * @param rrs A (@link RangeReadings} object containing a set of {@link RangeReading} values.
	 * @param pose the pose of the robot when the reading was taken
	 */
	public RangeFeature(RangeReadings rrs, Pose pose) {
		// TODO: This assumes index 0 is closest range. Should really check for the one with the shortest range.
		// Might also want to make sure this is not empty.
		this(rrs.get(0));
		this.rrs = rrs;
		this.pose = pose;
	}
	
	public RangeReading getRangeReading() {
		return rr;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public RangeReadings getRangeReadings() {
		if(rrs == null) {
			rrs = new RangeReadings(0);
			rrs.add(rr);
		}
		return rrs;
	}
	
	public Pose getPose() {
		return pose;
	}

	public void dumpObject(DataOutputStream dos) throws IOException {
		pose.dumpObject(dos);
		dos.writeFloat(rr.getRange());
		dos.writeLong(timeStamp);
		dos.flush();
	}

	public void loadObject(DataInputStream dis) throws IOException {
		pose.loadObject(dis);
		float range = dis.readFloat();
		rr = new RangeReading(0f,range);
		timeStamp = dis.readLong();		
	} 
}