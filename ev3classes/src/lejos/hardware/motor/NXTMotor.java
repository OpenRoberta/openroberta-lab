package lejos.hardware.motor;

import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.Encoder;
import lejos.robotics.EncoderMotor;

/**
 * Abstraction for an NXT motor with no speed regulation.
 * 
 */
public class NXTMotor extends BasicMotor implements EncoderMotor {
    protected Encoder encoderPort;

    /**
     * Create an instance of a NXTMotor using the specified motor port and
     * PWM operating mode.
     * @param port The motor port that the motor will be attached to.
     * @param PWMMode see {@link lejos.hardware.port.BasicMotorPort#PWM_FLOAT} and see {@link lejos.hardware.port.BasicMotorPort#PWM_BRAKE}
     */
    public NXTMotor(Port port, int PWMMode)
    {
        this(port.open(TachoMotorPort.class), PWMMode);
        releaseOnClose(this.port);
    }
    
    /**
     * Create an instance of a NXTMotor using the specified motor port the
     * PWM operating mode will be PWM_BREAK {@link lejos.hardware.port.BasicMotorPort#PWM_BRAKE}
     * @param port The motor port that the motor will be attached to.
     */
    public NXTMotor(Port port)
    {
        this(port, TachoMotorPort.PWM_BRAKE);
    }
    
    /**
     * Create an instance of a NXTMotor using the specified motor port and
     * PWM operating mode.
     * @param mport The motor port that the motor will be attached to.
     * @param PWMMode see {@link lejos.hardware.port.BasicMotorPort#PWM_FLOAT} and see {@link lejos.hardware.port.BasicMotorPort#PWM_BRAKE}
     */
    public NXTMotor(TachoMotorPort mport, int PWMMode)
    {
        this.port = mport;
        // We use extra var to avoid cost of a cast check later
        encoderPort = mport;
        mport.setPWMMode(PWMMode);
    }
    
    /**
     * Create an instance of a NXTMotor using the specified motor port the
     * PWM operating mode will be PWM_BREAK {@link lejos.hardware.port.BasicMotorPort#PWM_BRAKE}
     * @param port The motor port that the motor will be attached to.
     */
    public NXTMotor(TachoMotorPort port)
    {
        this(port, TachoMotorPort.PWM_BRAKE);
    }
    
    public int getTachoCount()
    {
        return encoderPort.getTachoCount();
    }

    public void resetTachoCount()
    {
        encoderPort.resetTachoCount();
    }
}
