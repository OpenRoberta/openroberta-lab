package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Support the HiTechnic Gyro sensor NGY1044 (or equivalent). 
 * See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NGY1044
 *
 * @author Lawrie Griffiths
 *
 */
public class HiTechnicGyro extends AnalogSensor implements SensorConstants, SensorMode {
	private static final float TO_SI=-1;
	private float zero = 600f;
	
    /**
     * Supports the SampleProvider interface. <br>
	 * The sensor measures the angular velocity in degrees per second. 
	 * A positive rate indicates a counterclockwise rotation. A negative rate indicates a clockwise rotation.
	 * 
     * @param port the Analog port
     */
	public HiTechnicGyro(AnalogPort port) {
		super(port);
	    init();
	}
	
	/**
     * Supports the SampleProvider interface. <br>
	 * The sensor measures the angular velocity in degrees per second. 
	 * A positive rate indicates a counterclockwise rotation. A negative rate indicates a clockwise rotation.
	 * 
	 * @param port the Sensor port
	 */
    public HiTechnicGyro(Port port) {
        super(port);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    	port.setTypeAndMode(TYPE_CUSTOM, MODE_RAW);
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		sample[offset] = ((float) NXTRawValue(port.getPin1()) - zero) * TO_SI;
	}

	@Override
	public String getName() {
		return "Gyro";
	}
}
