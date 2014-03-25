package lejos.robotics.navigation;

public interface MoveController extends MoveProvider {
	/**
	 * NXT 1.0 kit wheel diameter, in centimeters
	 */
	public static final double WHEEL_SIZE_NXT1 = 5.60;

	/**
	 * NXT 2.0 kit wheel diameter, in centimeters
	 */
	public static final double WHEEL_SIZE_NXT2 = 4.32;

	/**
	 * White RCX "motorcycle" wheel diameter, in centimeters
	 */
	public static final double WHEEL_SIZE_RCX  = 8.16;

	/**
	 *Starts the  NXT robot moving forward.
	 */
	public void forward();

	/**
	 *Starts the  NXT robot moving backwards.
	 */
	public void backward();

	/**
	 * Halts the NXT robot
	 */
	public void stop();

	/**
	 * true if the robot is moving 
	 * @return true if the robot is moving under power.
	 */
	public boolean isMoving();

	/**
	 * Moves the NXT robot a specific distance. A positive value moves it forward and a negative value moves it backward.
	 * Method returns when movement is done.
	 * 
	 * @param distance The positive or negative distance to move the robot.
	 */
	public void travel(double distance);

	/**
	 * Moves the NXT robot a specific distance. A positive value moves it forward and a negative value moves it backward.
	 * @param distance The positive or negative distance to move the robot, in wheel diameter units.
	 * @param immediateReturn If immediateReturn is true then the method returns immediately.
	 */
	public void travel(double distance, boolean immediateReturn);

	/**
	 * Sets the speed at which the robot will travel forward and backward (and to some extent arcs, although actual arc speed
	 * is slightly less). Speed is measured in units/second. e.g. If wheel diameter is cm, then speed is cm/sec.
	 * @param speed In chosen units per second (e.g. cm/sec)
	 */
	public void setTravelSpeed(double speed);

	/**
	 * Returns the speed at which the robot will travel forward and backward (and to some extent arcs, although actual arc speed
	 * is slightly less). Speed is measured in units/second. e.g. If wheel diameter is cm, then speed is cm/sec.
	 * @return Speed in chosen units per second (e.g. cm/sec)
	 */
	public double getTravelSpeed();

	/**
	 * Returns the maximum speed at which this robot is capable of traveling forward and backward.
	 * Speed is measured in units/second. e.g. If wheel diameter is cm, then speed is cm/sec.
	 * @return Speed in chosen units per second (e.g. cm/sec)
	 */
	public double getMaxTravelSpeed();

}
