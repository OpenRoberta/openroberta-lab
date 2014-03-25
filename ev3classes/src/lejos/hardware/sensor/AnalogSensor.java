package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

/**
 * Base class for analog sensor drivers
 * @author andy
 *
 */
public class AnalogSensor extends BaseSensor implements SensorConstants
{
    protected AnalogPort port;
    protected int currentType = -1;
    
    public AnalogSensor(AnalogPort p)
    {
        this.port = p;
    }
    
    public AnalogSensor(Port p, int type, int mode)
    {
        this(p.open(AnalogPort.class));
        if (!port.setTypeAndMode(type, mode))
        {
            port.close();
            throw new IllegalArgumentException("Invalid sensor mode");                
        }
        releaseOnClose(this.port);
    }

    public AnalogSensor(Port p)
    {
        this(p, TYPE_CUSTOM, MODE_RAW);
    }

    
 
    /**
     * Helper method. Take a voltage and return it as a normalized value in the
     * range 0.0-1.0
     * @param val input value
     * @return normalized value
     */
    protected float normalize(float val)
    {
        return val/EV3SensorConstants.ADC_REF;
    }

    /**
     * Return the equivalent NXT RAW sensor reading to the given voltage
     * @param val ADC voltage
     * @return The reading that would be returned on the NXT
     */
    protected float NXTRawValue(float val)
    {
        return val*SensorConstants.NXT_ADC_RES/EV3SensorConstants.ADC_REF;
    }
    
    /**
     * Return the equivalent NXT RAW sensor reading to the given voltage
     * @param val ADC voltage
     * @return The reading that would be returned on the NXT
     */
    protected int NXTRawIntValue(float val)
    {
        return (int)(NXTRawValue(val)+0.5);
    }


    /**
     * Switch to the selected type (if not already in that type) and delay for the
     * specified period to allow the sensor to settle in the new state. <br>
     * NOTE: This method is intended for use with NXT sensor drivers that use a
     * sensor type to specify the operating mode.
     * @param newType The type to switch to.
     * @param switchDelay Time in mS to delay after the switch.
     */
    protected void switchType(int newType, long switchDelay)
    {
        if (currentType != newType)
        {
            if (!port.setType(newType))
                throw new IllegalArgumentException("Invalid sensor mode");                
            currentType = newType;
            Delay.msDelay(switchDelay);
        }
    }
    //TODO: Add a switchPinMode or whatever method for use by EV3 sensors. At the moment
    // there are no EV3 analog sensors that are controlled by pin settings.
    

}
