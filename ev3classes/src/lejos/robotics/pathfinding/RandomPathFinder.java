package lejos.robotics.pathfinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.geometry.Point;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.mapping.RangeMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.navigation.WaypointListener;

/**
 * PathFinder that takes a map and a dummy set of range readings.
 * It finds a path that is in short moves, has no obstacles in the
 * way and where valid range readings can be taken from each waypoint.
 * 
 * The algorithm is not deterministic so each time it is called a new route 
 * will be found.
 * 
 * @author Lawrie Griffiths
 *
 */
public class RandomPathFinder implements PathFinder {	
	private static final long serialVersionUID = 1L;
	
	private float minGain = 1;	
	private float clearance = 20;	
	private RangeMap map;
	private RangeReadings readings;
	private float maxRange = 150;
	private float maxDistance = 200;
	private int maxIterations = 1000;
	
	private ArrayList<WaypointListener> listeners ;
	
	public RandomPathFinder(RangeMap map, RangeReadings readings) {
		this.map = map;
		this.readings = readings;
	}
	
	/**
	 * Set the maximum valid range readings
	 * @param range the maximum range
	 */
	public void setMaxRange(float range) {
		maxRange = range;
	}
	
	/**
	 * Set the maximum distance between waypoints
	 * @param distance the maximum distance
	 */
	public void setMaxDistance(float distance) {
		maxDistance = distance;
	}
	
	/**
	 * Set the maximum number of iterations before giving up when searching for a path
	 * @param iterations the maximum number of iterations
	 */
	public void setMaxIterations(int iterations) {
		maxIterations = iterations;
	}
	
	/**
	 * Set the clearance around the edge of the map.
	 * This does not really work as clearance shiould be around all
	 * walls and obstacles.
	 * 
	 * @param clearance the clearance
	 */
	public void setClearance(float clearance) {
		this.clearance = clearance;
	}
	
	public Path findRoute(Pose start, Waypoint destination)
			throws DestinationUnreachableException {
		Pose pose = start;
		Path route = new Path();
		
		// Continue until we return a route or throw DestinationUnReachableException
		for(;;) {
			// If the current pose if close enough to the destination, 
			// and there are no obstacles in the way, go straight there
			Pose testPose = pose;
			testPose.setHeading(testPose.angleTo(destination));
			if (testPose.distanceTo(destination) < maxDistance &&
					map.range(testPose) >= testPose.distanceTo(destination)) {
				route.add(new Waypoint(destination));
				return route;
			} 
			testPose = null;
			
			// Generate random poses and apply tests to them
			int i;
			for(i=0;i<maxIterations;i++) {
			    testPose = generatePose();
			    
			    // The new Pose must not be more than MAX_DISTANCE away from current pose	    
			    if (testPose.distanceTo(pose.getLocation()) > maxDistance) continue;
			    
				// The new pose must be at least MIN_GAIN closer to the destination
				if (pose.distanceTo(destination) - 
						testPose.distanceTo(destination) < minGain)
					continue;
				
				// We must be able to get a valid set of range readings from the new pose
				float heading = testPose.getHeading();
				boolean validReadings = true;
				for(RangeReading r: readings) {
					testPose.setHeading(heading + r.getAngle());
					float range = map.range(testPose);
					if (range > maxRange) {
						validReadings = false;
						break;
					}
				}					
				if (!validReadings) continue;
				
				//Check there are no obstacles in the way 
				testPose.setHeading(testPose.angleTo(pose.getLocation()));
				if (map.range(testPose) < testPose.distanceTo(pose.getLocation()))
					continue;
				//System.out.println("Range = " + map.range(testPose));
				//System.out.println("DistanceTo = " + testPose.distanceTo(pose.getLocation()));
				//System.out.println("From = " + pose);
				//System.out.println("To = " + testPose);
				
				testPose.setHeading(heading); // Restore heading
				break; // We have a new way point
			}
			if (i == maxIterations) throw new  DestinationUnreachableException();
			route.add(new Waypoint(testPose));
			pose = testPose;
		}
	}
	
	/**
	 * Generate a random pose within the mapped area, not too close to the edge
	 */
	private Pose generatePose() {
	    float x, y, heading;
	    Rectangle boundingRect = map.getBoundingRect();
	    Rectangle innerRect = new Rectangle(boundingRect.x + clearance, boundingRect.y + clearance,
	        boundingRect.width - clearance * 2, boundingRect.height - clearance * 2);

	    // Generate x, y values in bounding rectangle
	    for (;;) { // infinite loop that we break out of when we have
	               // generated a particle within the mapped area
	      x = innerRect.x + (((float) Math.random()) * innerRect.width);
	      y = innerRect.y + (((float) Math.random()) * innerRect.height);

	      if (map.inside(new Point(x, y))) break;
	    }

	    // Pick a random angle
	    heading = ((float) Math.random()) * 360;
	    
	    return new Pose(x,y,heading);
	}
	
	public void addListener(WaypointListener wpl) {
	    if(listeners == null )listeners = new ArrayList<WaypointListener>();
	    listeners.add(wpl);
	  }

	  public void startPathFinding(Pose start, Waypoint end) {
		  Collection<Waypoint> solution = null;
		try {
			solution = findRoute(start, end);
		} catch (DestinationUnreachableException e) {
			// TODO Not sure what the proper response is here. All in one.
			return;
		}
		if(listeners != null) { 
			for(WaypointListener l : listeners) {
				Iterator<Waypoint> iterator = solution.iterator(); 
				while(iterator.hasNext()) {
					l.addWaypoint(iterator.next());
				}
				l.pathGenerated();
			}
		}
	 }
}
