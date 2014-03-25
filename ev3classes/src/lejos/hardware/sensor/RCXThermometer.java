package lejos.hardware.sensor;

import lejos.hardware.port.LegacySensorPort;

/** 
 *Abstraction for an RCX temperature sensor. 
 * 
 * @author Soren Hilmer
 * 
 */
public class RCXThermometer extends AnalogSensor implements SensorConstants, SensorMode {
    LegacySensorPort port;
    
    /**
     * Create an RCX temperature sensor object attached to the specified port.
     * @param port port, e.g. Port.S1
     */
    public RCXThermometer(LegacySensorPort port)
    {
        super(port);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    	port.setTypeAndMode(TYPE_TEMPERATURE, MODE_RAW);
    }
    
    /**
     * Return a sample provider in temperature mode
     */
    public SensorMode getTemperatureMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		sample[offset] = (785-NXTRawValue(port.getPin1()))/8.0f +273.15f; // Kelvin
	}

	@Override
	public String getName() {
		return "Temperature";
	}
}
