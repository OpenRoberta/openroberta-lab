package lejos.robotics.pathfinding;

import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.navigation.WaypointListener;

/**
 * 
 * This class creates a set of waypoints connected by straight lines that lead from one location to another without
 * colliding with mapped geometry. 
 *
 */
public interface PathFinder {
	Path findRoute(Pose start, Waypoint destination) throws DestinationUnreachableException;
	
	public void addListener(WaypointListener wpl);
	
	public void startPathFinding(Pose start, Waypoint end);
}
