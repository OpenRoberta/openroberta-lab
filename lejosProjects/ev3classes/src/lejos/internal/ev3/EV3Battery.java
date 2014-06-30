package lejos.internal.ev3;

import lejos.hardware.Power;
import lejos.hardware.sensor.EV3SensorConstants;

/**
 * Class which provides information about the EV3 battery.<br>
 * TODO: The motor current and battery temp. values do not seem to be valid.
 * Check on other EV3s to see if they are different.
 * @author andy
 *
 */
public class EV3Battery implements Power
{
    protected final static float SHUNT_IN = 0.11f;
    protected final static float AMP_CIN = 22.0f;
    protected final static float SHUNT_OUT = 0.055f;
    protected final static float AMP_COUT = 19.0f;
    protected final static float VCE = 0.05f;
    protected final static float AMP_VIN = 0.5f;
    /**
     * Convert from ADC reading to actual units.
     * @param val
     * @return
     */
    protected float convert(int val)
    {
        return((float)val*EV3SensorConstants.ADC_REF)/(EV3SensorConstants.ADC_RES);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getVoltageMilliVolt()
    {
        // TODO Auto-generated method stub
        return (int)(getVoltage()*1000f);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getVoltage()
    {
        float CinV = convert(EV3AnalogPort.getBatteryCurrent())/AMP_CIN;
        return convert(EV3AnalogPort.getBatteryVoltage())/AMP_VIN + CinV + VCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getBatteryCurrent()
    {
        return (convert(EV3AnalogPort.getBatteryCurrent())/AMP_CIN)/SHUNT_IN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getMotorCurrent()
    {
        return (convert(EV3AnalogPort.getMotorCurrent())/AMP_COUT)/SHUNT_OUT;
    }

}
