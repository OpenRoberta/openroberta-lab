package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.EndianTools;

/**
 * This class support the Digital Pneumatic Pressure Sensor (PPS58-Nx) by MindSensors.
 * 
 * See http://www.mindsensors.com/index.php?module=pagemaster&PAGE_user_op=view_page&PAGE_id=150
 * 
 */
public class MindSensorsPressureSensor extends I2CSensor implements SensorMode {

	/*
	 * Code contributed and tested by fussel_dlx on the forums:
	 * http://lejos.sourceforge.net/forum/viewtopic.php?f=6&t=4329
	 * 
	 * Comment: the sensor can pressure in various units. However, using those
	 * units results in a loss of precision. And furthermore, the conversion to PSI or
	 * whatever can be done in Java. The obvious advantage is, that float can be used.
	 */
	
	private static final int ADDRESS = 0x18; 
	private final byte[] buf = new byte[4];
	
    public MindSensorsPressureSensor(I2CPort port) {
        // also works with high speed mode
        super(port, ADDRESS);
        init();
    }
    
    public MindSensorsPressureSensor(Port port) {
        // also works with high speed mode
        super(port, ADDRESS);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    }
    
    /**
     * Return a ample provider for pressure mode
     */
    public SensorMode getPressureMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(0x53, buf, 0, 4);		
		sample[offset] = (float) EndianTools.decodeIntLE(buf, 0);
	}

	@Override
	public String getName() {
		return "Pressure";
	}
}
