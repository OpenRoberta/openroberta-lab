package lejos.hardware.port;

import lejos.hardware.motor.MotorRegulator;
import lejos.robotics.Encoder;

/**
 * Abstraction for a motor port that supports NXT motors with tachometers.
 * 
 * @author Lawrie Griffiths
 *
 */
public interface TachoMotorPort extends BasicMotorPort, Encoder {
    /**
     * Return the motor regulator associated with this motor port.
     * @return the motor regulator for this port.
     */
    public MotorRegulator getRegulator();
}