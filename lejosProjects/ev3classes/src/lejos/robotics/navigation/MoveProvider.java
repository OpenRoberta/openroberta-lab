package lejos.robotics.navigation;

import lejos.robotics.navigation.Move;

/**
 * Should be implemented by a Pilot that provides a partial movement to a pose
 * when requested.
 *
 * @author nxj team
 */
public interface MoveProvider {
  
	/**
	 * Returns the move made since the move started, but before it has completed. This method is used
	 * by GUI maps to display the movement of a robot in real time. The robot must be capable of determining
	 * the move while it is in motion.  	
	 * @return The move made since the move started.
	 */
	public Move getMovement();
  
  /**
   * Adds a MoveListener that will be notified of all movement events.
   * @param listener the move listener
   */
  public void addMoveListener(MoveListener listener);
}
