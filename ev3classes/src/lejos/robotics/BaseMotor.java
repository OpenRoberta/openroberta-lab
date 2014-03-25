package lejos.robotics;

/**
 * Base motor interface. Contains basic movement commands.
 *
 */
public interface BaseMotor {

	/**
	   * Causes motor to rotate forward until <code>stop()</code> or <code>flt()</code> is called.
	   */
	  void forward();

	  /**
	   * Causes motor to rotate backwards until <code>stop()</code> or <code>flt()</code> is called.
	   */
	  void backward();

	  /**
	   * Causes motor to stop immediately. It will resist any further motion. Cancels any rotate() orders in progress.
	   */
	  void stop();

	  /**
	   * Motor loses all power, causing the rotor to float freely to a stop.
	   * This is not the same as stopping, which locks the rotor.
	   */
	  public void flt();

	  /**
	   * Return <code>true</code> if the motor is moving.
	   *
	   * @return <code>true</code> if the motor is currently in motion, <code>false</code> if stopped.
	   */
	  // TODO: Possibly part of Encoder interface? Depends if encoder used to determine this.
	  boolean isMoving();

}
