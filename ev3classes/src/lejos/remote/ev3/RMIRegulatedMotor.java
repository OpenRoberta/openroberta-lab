package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.robotics.RegulatedMotorListener;

public interface RMIRegulatedMotor extends Remote {

	  /**
	   * Adds a listener object that will be notified when rotation has started or stopped 
	   * @param listener
	   */
		//TODO method name sounds like listener is added to some list.
		// javadoc and method name should be changed such that they indicate that only one listener is supported.
		// suggested method name: setListener(...)
		public void addListener(RegulatedMotorListener listener) throws RemoteException;

		/**
		 * Removes the RegulatedMotorListener from this class.
		 * @return The RegulatedMotorListener that was removed, if any. Null if none existed.
		 */
		public RegulatedMotorListener removeListener() throws RemoteException;
		
	    /**
	     * Causes motor to stop, pretty much
	     * instantaneously. In other words, the
	     * motor doesn't just stop; it will resist
	     * any further motion.
	     * Cancels any rotate() orders in progress
	     * @param immediateReturn if true do not wait for the motor to actually stop
	     */
	    public void stop(boolean immediateReturn) throws RemoteException;

	    /**
	     * Set the motor into float mode. This will stop the motor without braking
	     * and the position of the motor will not be maintained.
	     * @param immediateReturn If true do not wait for the motor to actually stop
	     */
	    public void flt(boolean immediateReturn) throws RemoteException;

	    /**
	     * Wait until the current movement operation is complete (this can include
	     * the motor stalling).
	     */
	    public void waitComplete() throws RemoteException;

		
	  /**
	   * causes motor to rotate through angle; <br>
	   * iff immediateReturn is true, method returns immediately and the motor stops by itself <br>
	   * If any motor method is called before the limit is reached, the rotation is canceled. 
	   * When the angle is reached, the method isMoving() returns false;<br>
	   * 
	   * @param  angle through which the motor will rotate
	   * @param immediateReturn iff true, method returns immediately, thus allowing monitoring of sensors in the calling thread. 
	   * 
	   *  @see lejos.robotics.RegulatedMotor#rotate(int, boolean)
	   */
	  void rotate(int angle, boolean immediateReturn) throws RemoteException;

	  /**
	   * Causes motor to rotate by a specified angle. The resulting tachometer count should be within +- 2 degrees on the NXT.
	   * This method does not return until the rotation is completed.
	   * 
	   * @param angle by which the motor will rotate.
	   * 
	   */
	  void rotate(int angle) throws RemoteException;

	  
	  /**
	   * Causes motor to rotate to limitAngle;  <br>
	   * Then getTachoCount should be within +- 2 degrees of the limit angle when the method returns
	   * @param  limitAngle to which the motor will rotate, and then stop (in degrees). Includes any positive or negative int, even values &gt; 360.
	   */
	  public void rotateTo(int limitAngle) throws RemoteException;
	  
	  /**
	   * causes motor to rotate to limitAngle; <br>
	   * if immediateReturn is true, method returns immediately and the motor stops by itself <br> 
	   * and getTachoCount should be within +- 2 degrees if the limit angle
	   * If any motor method is called before the limit is reached, the rotation is canceled. 
	   * When the angle is reached, the method isMoving() returns false;<br>
	   * @param  limitAngle to which the motor will rotate, and then stop (in degrees). Includes any positive or negative int, even values &gt; 360. 
	   * @param immediateReturn iff true, method returns immediately, thus allowing monitoring of sensors in the calling thread.
	   */
	  public void rotateTo(int limitAngle,boolean immediateReturn) throws RemoteException;  

	  /**
	   * Return the limit angle (if any)
	   * @return the current limit angle
	   */
	  public int getLimitAngle() throws RemoteException;

	  /**
	   * Set motor speed. As a rule of thumb 100 degrees per second are possible for each volt on an NXT motor. Therefore,
	   * disposable alkaline batteries can achieve a top speed of 900 deg/sec, while a rechargable lithium battery pack can achieve
	   * 740 deg/sec. 
	   * 
	   * @param speed in degrees per second.
	   */
	  void setSpeed(int speed) throws RemoteException;

	  /**
	   * Returns the current motor speed.
	   * 
	   * @return motor speed in degrees per second
	   */
	  int getSpeed() throws RemoteException;
	 
	  /**
	   * Returns the maximim speed of the motor.
	   * 
	   * @return the maximum speed of the Motor in degrees per second.
	   */
	  float getMaxSpeed() throws RemoteException;
	  
	  /**
	   * returns true if motor is stalled
	   * @return true if stalled
	   */
	   boolean isStalled() throws RemoteException;
	   
	   /**
	    * Set the parameters for detecting a stalled motor. A motor will be recognized as 
	    * stalled if the movement error (the amount the motor lags the regulated position) 
	    * is greater than error for a period longer than time.
		*
		* @param error The error threshold
	    * @param time The time that the error threshold needs to be exceeded for.
	    */
	   void setStallThreshold(int error, int time) throws RemoteException; 
	   
	   /**
	    * Set the required rate of acceleration degrees/s/s
	    * @param acceleration
	    */
	   void setAcceleration(int acceleration) throws RemoteException;
	   
	   void close() throws RemoteException;
	   
	   void forward() throws RemoteException;
	   
	   void backward() throws RemoteException;
	   
	   void resetTachoCount() throws RemoteException;
	   
	   int getTachoCount() throws RemoteException;
	   
	   boolean isMoving() throws RemoteException;

}

