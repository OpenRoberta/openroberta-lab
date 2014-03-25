package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Basic sensor driver for the Lego EV3 Touch sensor
 * @author andy
 *
 */
public class EV3TouchSensor extends AnalogSensor implements SensorMode
{
    
    public EV3TouchSensor(AnalogPort port)
    {
        super(port);
        init();
    }

    public EV3TouchSensor(Port port)
    {
        super(port);
        init();
    }
    
    protected void init() {
      setModes(new SensorMode[]{ this }); 
    }

    public SensorMode getTouchMode()
    {
        return this;
    }
    
    @Override
    public int sampleSize()
    {
        return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset)
    {
        sample[offset] = (port.getPin6() > EV3SensorConstants.ADC_REF/2f ? 1.0f : 0.0f);
       
    }

    @Override
    public String getName()
    {
       return "Touch";
    }

}
