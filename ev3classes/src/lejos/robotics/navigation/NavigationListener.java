package lejos.robotics.navigation;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

/**
 * Interface for informing listeners that a way point has been reached.
 * 
 */
public interface NavigationListener
{
 
   /**
    * Called when the robot  has reached a new Wahpoint.
    * @param waypoint where the robot
    * @param pose of the robot
    * @param sequence of the Waypoint in the path
    */
  public void atWaypoint(Waypoint waypoint, Pose pose, int sequence);
  
/**
   * Called when the robot has reached the last Waypoint of the path
    * @param waypoint where the robot
    * @param pose of the robot
    * @param sequence of the Waypoint in the path 
   */
  public void pathComplete(Waypoint waypoint, Pose pose, int sequence);  
 
/**
   *  called when the robot has stopped, not at a Waypoint 
   * @param waypoint toward which the robot was moving
   * @param pose   current pose of the robot
   * @param sequence number of the next Waypoint
   */
  public void pathInterrupted(Waypoint waypoint, Pose pose, int sequence);
}
