package lejos.robotics.navigation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.robotics.Transmittable;
import lejos.robotics.geometry.Point;

/**
 * A sequence of way points make up a route that a robot can navigate.
 * 
 * Waypoint extends Point, as a way point can just be a point.
 * 
 * However, a Waypoint can optionally specify a heading that the robot must achieve when it reaches the  way points.
 * 
 * It can also optionally specify how close the robot must get to the way point in order for it to be deemed to
 * have reached it.
 * 
 */
public class Waypoint extends Point implements Transmittable {
	protected float heading = 0;
	protected boolean headingRequired;
	protected float maxPositionError = -1;
	protected float maxHeadingError = -1;
	
	public Waypoint(double x, double y) {
		super((float)x,(float)y);
		headingRequired = false; 
	}
	
	public Waypoint(double x, double y, double heading) {
		super((float)x,(float)y);
		headingRequired = true;
		this.heading = (float)heading;
	}
	
	public Waypoint(Point p) {
		super((float) p.getX(),(float) p.getY());
		headingRequired = false;
	}
	
	public Waypoint(Pose p) {
		super(p.getX(),p.getY());
		headingRequired = true;
		this.heading = p.getHeading();
	}
	
	public double getHeading() {
		return heading;
	}
	
	public boolean isHeadingRequired() {
		return headingRequired;
	}
	
	public double getMaxPositionError() {
		return maxPositionError;
	}
	
	public void setMaxPositionError(double distance) {
		maxPositionError = (float)distance;
	}
	
	public double getMaxHeadingError() {
		return maxHeadingError;
	}
	
	public void setMaxHeadingError(double distance) {
		maxHeadingError = (float)distance;
	}
	
	/**
	 * Return a Pose that represents the way point. If no header is specified, it is set to zero.
	 * 
	 * @return the pose corresponding to the way point
	 */
	public Pose getPose() {
		return new Pose(x,y,heading);
	}
	
	/**
	 * Check that the given pose satisfies the conditions for this way point
	 * @param p the Pose
	 */
	public boolean checkValidity(Pose p) {
		if (maxPositionError >= 0 && 
		    p.distanceTo(this) > maxPositionError) return false;
		if (headingRequired && maxHeadingError >= 0 && 
			    Math.abs(p.getHeading() - heading) > maxHeadingError) return false;
		return true;
	}

	public void dumpObject(DataOutputStream dos) throws IOException {
		dos.writeFloat(x);
		dos.writeFloat(y);
		dos.writeFloat(heading);
		dos.writeBoolean(headingRequired);
		dos.flush();
	}

	public void loadObject(DataInputStream dis) throws IOException {
		x = dis.readFloat();
		y = dis.readFloat();
		heading = dis.readFloat();
		headingRequired = dis.readBoolean();	
	}
}
