package lejos.hardware.device.tetrix;

import lejos.robotics.Servo;

/** 
 * Basic servo motor abstraction. Servos are driven by a PWM signal from the controller with varying pulse widths
 * controlling the rotational position of the servo actuator shaft.
 * <P>
 * The HiTechnic Servo Controller allows setting of the PWM output from 0.75 - 2.25ms. Note that some servos may hit their 
 * internal mechanical limits at each end of this range causing them to consume excessive current and <b>potentially be damaged</b>.
 * 
 * @author Kirk P. Thompson
 */
public class TetrixServo implements Servo{
    private TetrixServoController sc;
    private int channel;
    
    TetrixServo(TetrixServoController sc, int channel) {
        this.sc=sc;
        this.channel=channel;
    }

    /**
     * Set the allowable pulse width operating range of this servo in microseconds and the total travel range. 
     * Default for pulse width at instantiation is 750 & 2250 microseconds. Default for travel is 200 degrees. 
     * <p>
     * The midpoint
     * of the pulse width operating range should normally be 1500 microseconds so the range extents should reflect this.
     * <p> 
     * This information must reflect the appropriate specifications and/or empirical characterization data of the specific 
     * servo used for the <code>setAngle()</code> method to be able to position the servo accurately.
     * 
     * @param microsecLOW The low end of the servos response/operating range in microseconds
     * @param microsecHIGH The high end of the servos response/operating range in microseconds
     * @param travelRange The total mechanical travel range of the servo in degrees
     * @throws IllegalArgumentException if the range isn't within 750 and 2250
     * @see #setAngle
     */
    public void setRange (int microsecLOW, int microsecHIGH, int travelRange) throws IllegalArgumentException {
            sc.setPulseRange(channel, microsecLOW, microsecHIGH, travelRange);
    }
    
    /**
     * Sets the angle target of the servo. The positional accuracy of this method requires that <code>setRange()</code> 
     * be called with the
     * correct parameters to establish proper ranging conversion to internal controller representation of servo position.
     * 
     * @param angle Set servo angle in degrees.
     * @see #setRange
     * @see #getAngle
     */
    public void setAngle(float angle){
         sc.setAngle(channel, angle);
    }
    
    /**
     * Returns the current servo angle as reverse calculated by the last call to <code>setAngle()</code>. This is calculated from the
     * internal byte representation used (calculated by <code>setAngle()</code>) to control the servo so the resolution will be 
     * affected by ranging factors set with <code>setRange()</code>
     * <p>
     * The actual physical servo position may or may not be at the reported angle if mechanical limits have been reached.
     * 
     * @return Current servo angle
     * @see #setRange
     * @see #setAngle
     */
    public float getAngle(){
        return sc.getAngle(channel);
    }

    /**
     * Set the PWM pulse width for the servo. This must be in the range defined for the servo. This method allows manipulation
     * of the servo position based on absolute pulse widths in microseconds.
     * <p>
     * A servo pulse of 1500 microseconds (1.5 ms) width will typically set the servo to its "neutral" position. This is the 
     * "standard pulse servo mode" used by all hobby analog servos. 
     * <p>
     * The HiTechic Servo Controller allows setting of the PWM output from 750 to 2250 microseconds with a step resolution
     * of 5.88 microseconds (1 byte is used to control the pulse width output) and this method will calculate and use the stepped
     * value closest to value passed in <code>microSeconds</code>. 
     * <p>Note that some  servos may hit their internal mechanical limits at each end of this range causing them to consume excessive current 
     * and <i><b>potentially be damaged</b></i>.
     * 
     * @param microSeconds The pulse width time in microseconds
     * @throws IllegalArgumentException if the range isn't within 750 and 2250
     * @see #setRange
     */
    public void setpulseWidth(int microSeconds) throws IllegalArgumentException {
        sc.setPulseWidth(channel, microSeconds);
    }
    
    public int getpulseWidth(){
        return sc.getPulseWidth(channel);
    }
}
