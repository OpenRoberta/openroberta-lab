package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Abstraction for a NXT touch sensor.
 * Also works with RCX touch sensors.
 * 
 */
public class NXTTouchSensor extends AnalogSensor implements SensorConstants, SensorMode
{
	
	/**
	 * Create a touch sensor object attached to the specified open port. Note this
	 * port will not be configured. Any configuration od the sensor port must take
	 * place externally.
	 * @param port an open Analog port
	 */
	public NXTTouchSensor(AnalogPort port)
	{
	   super(port);
	   port.setTypeAndMode(TYPE_SWITCH, MODE_RAW);
	}

	/**
	 * Create an NXT touch sensor object attached to the specified port.
	 * @param port the port that has the sensor attached
	 */
	public NXTTouchSensor(Port port)
	{
	    super(port);
	    this.port.setTypeAndMode(TYPE_SWITCH, MODE_RAW);	    
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
        sample[offset] = (port.getPin1() > EV3SensorConstants.ADC_REF/2f ? 0.0f : 1.0f);
    }

    @Override
    public String getName()
    {
        return "Touch";
    }
}
