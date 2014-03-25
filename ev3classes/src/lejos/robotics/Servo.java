package lejos.robotics;

/** 
 * Abstraction for a range-limited servo motor.
 * 
 * @author Kirk P. Thompson
 */
public interface Servo {
    /**
     * Sets the rotational position (angle) of a ranged servo. 
     * 
     * @param angle the target angle in degrees
     * @see #getAngle
     * @see #setRange
     */
    public void setAngle(float angle);
    
    /**
     * Gets the rotational position (angle) of a ranged servo. 
     *
     * @return the angle in degrees
     * @see #setAngle
     */
    public float getAngle();
    
    /**
     * Set the PWM pulse width for the servo. This must be in the range defined for the servo. This method allows manipulation
     * of the servo position based on absolute pulse widths in microseconds.
     * <p>
     * A servo pulse of 1500 microseconds (1.5 ms) width will typically set the servo to its "neutral" position. This is the 
     * "standard pulse servo mode" used by all hobby analog servos. 
     * 
     * @param microSeconds The pulse width time in microseconds
     */
    public void setpulseWidth(int microSeconds);
    
    /**
     * Get the current PWM pulse width for the servo. 
     * 
     * @return The pulse width time in microseconds
     */
    public int getpulseWidth();
    
    /**Set the allowable pulse width operating range of this servo in microseconds and the total travel range to allow
     * the use of angle-based parameters to control the servo.
     * <p>
     * This information
     * is used to calculate ansolute angles used by the <code>setAngle()</code> and <code>getAngle()</code> methods.
     * The midpoint
     * of the pulse width operating range should normally be 1500 microseconds so the range extents should reflect this.
     * <p> 
     * This information must reflect the appropriate specifications and/or empirical characterization data of the specific 
     * servo used for the <code>setAngle()</code> method to be able to position the servo accurately.
     * 
     * @param microsecLOW The low end of the servos response/operating range in microseconds
     * @param microsecHIGH The high end of the servos response/operating range in microseconds
     * @param travelRange The total mechanical travel range of the servo in degrees
     * @see #setAngle
     */
    public void setRange (int microsecLOW, int microsecHIGH, int travelRange);
}
