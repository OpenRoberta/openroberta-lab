package lejos.robotics.navigation;

/**
 * Interface for informing listeners that a way point has been generated.
 * 
 */
public interface WaypointListener
{
  /**
   * Called when the class providing waypoints generates a new waypoint.
   * @param wp the new waypoint
   */
  public void addWaypoint(Waypoint wp);
  
  /**
   * Called when generation of the path is complete
   */
  public void pathGenerated();
  
}
