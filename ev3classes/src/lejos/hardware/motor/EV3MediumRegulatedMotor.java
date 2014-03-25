package lejos.hardware.motor;

import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3SensorConstants;

/**
 * Abstraction for a Medium Lego EV3/NXT motor.
 * 
 */
public class EV3MediumRegulatedMotor extends BaseRegulatedMotor
{
    static final float MOVE_P = 8f;
    static final float MOVE_I = 0.04f;
    static final float MOVE_D = 8f;
    static final float HOLD_P = 8f;
    static final float HOLD_I = 0.02f;
    static final float HOLD_D = 0f;
    static final int OFFSET = 1000;

    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public EV3MediumRegulatedMotor(TachoMotorPort port)
    {
        super(port, null, EV3SensorConstants.TYPE_MINITACHO, MOVE_P, MOVE_I, MOVE_D,
                HOLD_P, HOLD_I, HOLD_D, OFFSET);
    }
    
    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public EV3MediumRegulatedMotor(Port port)
    {
        super(port, null, EV3SensorConstants.TYPE_NEWTACHO, MOVE_P, MOVE_I, MOVE_D,
                HOLD_P, HOLD_I, HOLD_D, OFFSET);
    }


}
