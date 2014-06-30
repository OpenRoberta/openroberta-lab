package lejos.robotics.navigation;

/**
 * An enhanced MoveController that is capable of traveling in arcs.
 * @author NXJ Team
 *
 */
public interface ArcMoveController extends MoveController {
	
	/**
	 * The minimum steering radius this vehicle is capable of when traveling in an arc.
	 * Theoretically this should be identical for both forward and reverse travel. In practice?
	 * 
	 * @return the radius in degrees
	 */
	public double getMinRadius();
	
	/**
	 * Set the radius of the minimum turning circle.
	 * 
	 * @param radius the radius in degrees
	 */
	public void setMinRadius(double radius);

	/**
	  * Starts the NXT robot moving forward along an arc with a specified radius.
	  * <p>
	  * If <code>radius</code> is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.<br>
	  * If <code>radius</code> is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.<br>
	  * If <code>radius</code> is zero, the robot rotates in place.
	  * <p>
	  * Postcondition: Motor speeds are unpredictable.
	  * <p>
	  * Note: If you have specified a drift correction in the constructor it will not be applied in this method.
	  * 
	  * @param radius of the arc path. If positive, the left side of the robot is on the inside of the turn. If negative, the left
	  *          side of the robot is on the outside of the turn.
	  */
	 public void arcForward(double radius);

	 /**
	  * Starts the NXT robot moving backward along an arc with a specified radius.
	  * <p>
	  * If <code>radius</code> is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.<br>
	  * If <code>radius</code> is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.<br>
	  * If <code>radius</code> is zero, the robot rotates in place.
	  * <p>
	  * Postcondition: Motor speeds are unpredictable.
	  * <p>
	  * Note: If you have specified a drift correction in the constructor it will not be applied in this method.
	  * 
	  * @param radius of the arc path. If positive, the left side of the robot is on the inside of the turn. If negative, the left
	  *          side of the robot is on the outside of the turn.
	  */
	 public void arcBackward(double radius);
	 
	/**
	 * Moves the NXT robot along an arc with a specified radius and  angle,
	 * after which the robot stops moving. This method does not return until the robot has
	 * completed moving <code>angle</code> degrees along the arc.
	 * <p>
	 * If <code>radius</code> is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.<br>
	 * If <code>radius</code> is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.<br>
	 * If <code>radius</code> is zero, is zero, the robot rotates in place.
	 * <p>
	 * Robot will stop when the degrees it has moved along the arc equals <code>angle</code>.<br> 
	 * If <code>angle</code> is positive, the robot will turn to the left (anti-clockwise).<br>
	 * If <code>angle</code> is negative, the robot will turn to the right (clockwise).
	 * If <code>angle</code> is zero, the robot will not move and the method returns immediately.
	 * <p>
	 * Postcondition: Motor speeds are unpredictable.
	 * <p>
	 * Note: If you have specified a drift correction in the constructor it will not be applied in this method.
	 * 
	 * @param radius of the arc path. If positive, the left side of the robot is on the inside of the turn. If negative, the left
	 *          side of the robot is on the outside of the turn.
	 * @param angle The sign of the angle determines the direction of the robot turns: Positive is anti-clockwise,  negative is clockwise.
	 * @see #travelArc(double, double)
	 */
	 public void arc(double radius, double angle);

	/**
	 * Moves the NXT robot along an arc with a specified radius and  angle,
	 * after which the robot stops moving. This method has the ability to return immediately
	 * by using the <code>immediateReturn</code> parameter. 
	 * <p>
	 * If <code>radius</code> is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.<br>
	 * If <code>radius</code> is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.<br>
	 * If <code>radius</code> is zero, is zero, the robot rotates in place.
	 * <p>
	 * The robot will stop when the degrees it has moved along the arc equals <code>angle</code>.<br> 
     * If <code>angle</code> is positive, the robot will turn to the left (anti-clockwise).<br>
	 * If <code>angle</code> is negative, the robot will turn to the right (clockwise).
	 * If <code>angle</code> is zero, the robot will not move and the method returns immediately. 
	 * <p>
	 * Postcondition: Motor speeds are unpredictable.
	 * <p>
	 * Note: If you have specified a drift correction in the constructor it will not be applied in this method.
	 * 
	 * @param radius of the arc path. If positive, the left side of the robot is on the inside of the turn. If negative, the left
	 *          side of the robot is on the outside of the turn.
	 * @param angle The sign of the angle determines the direction of the robot turns: Positive is anti-clockwise,  negative is clockwise.
	 * @param immediateReturn If immediateReturn is true then the method returns immediately. 
	 * @see #travelArc(double, double, boolean)
	 */
	public void arc(double radius, double angle, boolean immediateReturn);

	/**
	 * Moves the NXT robot a specified distance along an arc of specified radius,
	 * after which the robot stops moving. This method does not return until the robot has
	 * completed moving <code>distance</code> along the arc. The units (inches, cm) for <code>distance</code> 
	 * must be the same as the units used for <code>radius</code>.
	 * <p>
	 * If <code>radius</code> is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.<br>
	 * If <code>radius</code> is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.<br>
	 * If <code>radius</code> is zero, the robot rotates in place
	 * <p>
	 * The robot will stop when it has moved along the arc <code>distance</code> units.<br> 
	 * If <code>distance</code> is positive, the robot will move travel forwards.<br>
	 * If <code>distance</code> is negative, the robot will move travel backwards.<br>
	 * If <code>distance</code> is zero, the robot will not move and the method returns immediately.
	 * <p>
	 * Postcondition: Motor speeds are unpredictable.
	 * <p>
	 * 
	 * @param radius of the arc path. If positive, the left side of the robot is on the inside of the turn. If negative, the left
	 *          side of the robot is on the outside of the turn.
	 * @param distance to travel, in same units as <code>radius</code>. The sign of the distance determines the direction of robot motion. Positive drives the robot forward, negative drives it backward.
	 * @see #arc(double, double)
	 * 
	 */
	 public void travelArc(double radius, double distance);

	/**
	 * Moves the NXT robot a specified distance along an arc of specified radius,
	 * after which the robot stops moving. This method has the ability to return immediately
	 * by using the <code>immediateReturn</code> parameter.  
	 * The units (inches, cm) for <code>distance</code> should be the same as the units used for <code>radius</code>.
	 * 
	 * <p>
	 * If <code>radius</code> is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.<br>
	 * If <code>radius</code> is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.<br>
	 * If <code>radius</code> is zero, the robot rotates in place.
	 * <p>
	 * The robot will stop when it has moved along the arc <code>distance</code> units.<br> 
	 * If <code>distance</code> is positive, the robot will move travel forwards.<br>
	 * If <code>distance</code> is negative, the robot will move travel backwards.<br>
	 * If <code>distance</code> is zero, the robot will not move and the method returns immediately.
	 * <p>
	 * Postcondition: Motor speeds are unpredictable.
	 * <p>
	 * 
	 * @param radius of the arc path. If positive, the left side of the robot is on the inside of the turn. If negative, the left
	 *          side of the robot is on the outside of the turn.
	 * @param distance to travel, in same units as <code>radius</code>. The sign of the distance determines the direction of robot motion. Positive drives the robot forward, negative drives it backward.
	 * @param immediateReturn If immediateReturn is true then the method returns immediately. 
	 * @see #arc(double, double, boolean)
	 * 
	 */
	public void travelArc(double radius, double distance, boolean immediateReturn);
}
