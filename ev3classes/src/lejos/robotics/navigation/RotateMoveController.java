package lejos.robotics.navigation;

public interface RotateMoveController extends MoveController {
  /**
   * Rotates the NXT robot the specified number of degrees; direction determined by the sign of the parameter.
   * Method returns when rotation is done.
   * 
   * @param angle The angle to rotate in degrees. A positive value rotates left, a negative value right (clockwise).
   */
  public void rotate(double angle);

  /**
   * Rotates the NXT robot the specified number of degrees; direction determined by the sign of the parameter.
   * Motion stops  when rotation is done.
   * 
   * @param angle The angle to rotate in degrees. A positive value rotates left, a negative value right (clockwise).
   * @param immediateReturn If immediateReturn is true then the method returns immediately
   */
  public void rotate(double angle, boolean immediateReturn);
  
  /**
   * sets the rotation speed of the robot (the angular velocity of the rotate()
   * methods)
   * @param speed in degrees per second
   */
  public void setRotateSpeed(double speed);
  
  /**
   * Returns the value of the rotation speed
   * @return the rotate speed in degrees per second
   */
  public double getRotateSpeed();

  /**
   * returns the maximum value of the rotation speed;
   * @return max rotation speed
   */
  public double getRotateMaxSpeed();
}
