package lejos.robotics.navigation;

import lejos.robotics.navigation.Move;

/**
 * <p>Any class that wants to be updated automatically by a MoveProvider should
 * implement this interface. Both movementStarted() and movementStopped() also return 
 * a MoveProvider object.</p>
 * 
 * <p>There are several practical scenarios to use a MoveListener. If you have a robot that has a range scanner
 * pointed forward and mounted on a motor, a MoveListener could listen for arc movements and rotate the scanner
 * left or right if the vehicle begins steering around a corner so the sensor is pointed where the robot is traveling.</p>
 * 
 * <p> In another scenario, a MoveListener GUI can listen for movements from multiple MovementProviders. It 
 * might want to draw one robot as blue, one as green, one as red, etc..
 * The MoveProvider allows it to differentiate the MovementProviders from one another.</p>
 * 
 * @author nxj team
 */
public interface MoveListener {
	
	/**
	 * Called when a Move Provider starts a move
	 *  
	 * @param event the movement
	 * @param mp the movement provider
	 */
	public void moveStarted(Move event, MoveProvider mp);
	
	/**
	 * Called by the movement provider when a move stops
	 * 
	 * @param event the movement
	 * @param mp movement provider
	 */
	public void moveStopped(Move event, MoveProvider mp);
}
