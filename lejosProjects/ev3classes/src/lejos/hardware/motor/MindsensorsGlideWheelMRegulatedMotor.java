package lejos.hardware.motor;

import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3SensorConstants;

/**
 * Abstraction for a Mindsensors Glidewheel-M equipped PF motor.<br>
 * Note: These settings are for the "M" motor. This motor does not seem
 * to like running slowly, or to use acceleration values less than around
 * 500deg/s/s. Also tested with the "XL" motor which seems to work well other
 * than some oscillation when holding. We may need different settings for
 * the two.
 * TODO: Find some way to make this work with an I term for hold
 * TODO: Can probably be tuned better then this.
 * 
 */
public class MindsensorsGlideWheelMRegulatedMotor extends BaseRegulatedMotor
{
    static final float MOVE_P = 3.5f;
    static final float MOVE_I = 0.01f;
    static final float MOVE_D = 3f;
    static final float HOLD_P = 2.5f;
    static final float HOLD_I = 0f;
    static final float HOLD_D = 0f;
    static final int OFFSET = 0;

    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public MindsensorsGlideWheelMRegulatedMotor(TachoMotorPort port)
    {
        super(port, null, EV3SensorConstants.TYPE_MINITACHO, MOVE_P, MOVE_I, MOVE_D,
                HOLD_P, HOLD_I, HOLD_D, OFFSET);
    }
    
    /**
     * Use this constructor to assign a variable of type motor connected to a particular port.
     * @param port  to which this motor is connected
     */
    public MindsensorsGlideWheelMRegulatedMotor(Port port)
    {
        super(port, null, EV3SensorConstants.TYPE_NEWTACHO, MOVE_P, MOVE_I, MOVE_D,
                HOLD_P, HOLD_I, HOLD_D, OFFSET);
    }


}
