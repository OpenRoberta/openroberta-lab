package lejos.hardware.motor;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

/**
 * Interface for motor regulation
 * regulate velocity; also stop motor at desired rotation angle.

 **/
public interface MotorRegulator
{
    public static final int NO_LIMIT = 0x7fffffff;


    /**
     * Set the motion control parameters used by the regulator.
     * @param typ The type of motor
     * @param moveP The Proportional control value used while moving
     * @param moveI The integral control parameter used while moving
     * @param moveD The differential control parameter used while moving
     * @param holdP The Proportional control value used while holding position
     * @param holdI The integral control value used while holding position
     * @param holdD The differential control value used while holding position
     * @param offset Motor PWM offset value range 0-10000.
     */
    public void setControlParamaters(int typ, float moveP, float moveI, float moveD, float holdP, float holdI, float holdD, int offset);
    /**
     * Get the current hardware tachometer reading for the motor,
     * @return hardware reading
     */
    public int getTachoCount();
    
    /**
     * Reset the tachometer base value, after this call the tachometer will return
     * zero for the current position. Note that any in progress movements will be
     * aborted.
     */
    public void resetTachoCount();

    /**
     * Return true if the motor is currently active
     * @return True if the motor is moving.
     */
    public boolean isMoving();
    
    /**
     * Return the current velocity (in degrees/second) that the motor is currently
     * running at. Note that this value may be supplied from the internal
     * control model not from actually measuring the rotation speed. If the regulator
     * is functioning correctly this will closely match the actual velocity
     * @return velocity
     */
    public float getCurrentVelocity();
    
    /**
     * Set the stall detection parameters. The motor will be declared as
     * stalled if the error in the motor position exceeds the specified value for
     * longer than the given time.
     * @param error
     * @param time
     */
    public void setStallThreshold(int error, int time);

    /**
     * return the regulations models current position. 
     * @return the models current position
     */
    public float getPosition();

    /**
     * Initiate a new move and optionally wait for it to complete.
     * If some other move is currently executing then ensure that this move
     * is terminated correctly and then start the new move operation.
     * @param speed
     * @param acceleration
     * @param limit
     * @param hold
     * @param waitComplete
     */
    public void newMove(float speed, int acceleration, int limit, boolean hold, boolean waitComplete);

    /**
     * The target speed has been changed. Reflect this change in the
     * regulator.
     * @param newSpeed new target speed.
     */
    public void adjustSpeed(float newSpeed);

    /**
     * The target acceleration has been changed. Updated the regulator.
     * @param newAcc
     */
    public void adjustAcceleration(int newAcc);
    
    /**
     * Wait until the current movement operation is complete (this can include
     * the motor stalling).
     */
    public void waitComplete();
    
    /**
     * Add a motor listener. Move operations will be reported to this object.
     * @param motor
     * @param listener
     */
    public void addListener(RegulatedMotor motor, RegulatedMotorListener listener);
    
    public RegulatedMotorListener removeListener();


    /**
     * Return the angle that this Motor is rotating to.
     * @return angle in degrees
     */
    public int getLimitAngle();
    
    /**
     * Return true if the motor is currently stalled.
     * @return true if the motor is stalled, else false
     */
    public boolean isStalled();


}


