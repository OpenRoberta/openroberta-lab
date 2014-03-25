package lejos.robotics.subsumption;


/**
* The Behavior interface represents an object embodying a specific
* behavior belonging to a robot. Each behavior must define three things: <BR>
* 1) The circumstances to make this behavior seize control of the robot.
* e.g. When the touch sensor determines the robot has collided with an object.<BR>
* 2) The action to perform when this behavior takes control. 
* e.g. Back up and turn.<BR>
* 3) A way to quickly exit from the action when the Arbitrator selects  a higher
 * priority behavior to take control.
* These are represented by defining the methods takeControl(), action(),
* and suppress() respectively. <BR>
* A behavior control system has one or more Behavior objects. When you have defined
* these objects, create an array of them and use that array to initialize an
* Arbitrator object.
*
* @see Arbitrator
  
* @version 0.9  May 2011
*/
public interface Behavior {
   
   /**
   * The boolean return  indicates  if this behavior should seize control of the robot.
   * For example, a robot that reacts if a touch sensor is pressed: <BR>
   * public boolean takeControl() { <BR>
   *    return touch.isPressed(); <BR>
   * } <BR>
   * @return boolean  Indicates if this Behavior should seize control.
   */
   public boolean takeControl();
   
   /**
   * The code in action() represents the tasks  the robot performs when this
   * behavior becomes active. It can be as complex as navigating around a
   * room, or as simple as playing a tune.<BR>
   * <B>The contract for implementing this method is:</B><BR>
   *  If its task is  is complete, the method returns.
    * It also  <B> must </B> return promptly when the suppress() method
    * is called, for example by testing the boolean suppress flag.  <br>
    * When this method exits, the robot is in a safe state for another behavior
    * to run its action() method
   */
   public void action();
   
   /**
   * The code in suppress() should cause the current behavior to exit. <BR>
   * <B>The contract for implementing this method is:</B><BR>
   *  Exit  quickly, for example, just set boolean flag.
   */
   public void suppress();
   
}