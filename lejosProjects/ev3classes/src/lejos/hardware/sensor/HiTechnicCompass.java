package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.Calibrate;

/**
 * This class supports the <a href="http://www.hitechnic.com">HiTechnic</a> compass sensor.
 * 
 * See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NMC1034
 * 
 */
public class HiTechnicCompass extends I2CSensor implements Calibrate, SensorMode {
	private final static byte COMMAND = 0x41;	
	private final static byte BEGIN_CALIBRATION = 0x43;
	private final static byte END_CALIBRATION = 0x00; // Back to measurement mode
	
	byte[] buf = new byte[2];
	
    /**
     * Create a compass sensor object
     * @param port I2C port for the compass
     * @param address The I2C address used by the sensor
     */
	public HiTechnicCompass(I2CPort port, int address) {
		super(port, address);
		init();
	}

   /**
     * Create a compass sensor object
     * @param port I2C port for the compass
     */
    public HiTechnicCompass(I2CPort port) {
        super(port, DEFAULT_I2C_ADDRESS);
        init();
    }
     
    /**
     * Create a compass sensor object
     * @param port Sensor port for the compass
     * @param address The I2C address used by the sensor
     */
    public HiTechnicCompass(Port port, int address) {
        super(port, address);
        init();
    }
    
    /**
     * Create a compass sensor object
     * @param port Sensor port for the compass
     */
    public HiTechnicCompass(Port port) {
        super(port);
        init();
    }
    
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    }
    
    /**
     * Get a compass mode sensor provider
     * @return the sample provider
     */
    public SensorMode getCompassMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(0x42, buf, 2);
		sample[offset] = 359-(((buf[0] & 0xff)<< 1) + buf[1]);
	}
	
	@Override
	public String getName() {
		return "Compass";
	}
	
	// TODO: Rotate in any direction while calibrating? Specify.
	/**
	 * Starts calibration for the compass. Must rotate *very* 
	 * slowly, taking at least 20 seconds per rotation.
	 * 
	 * Should make 1.5 to 2 full rotations.
	 * Must call stopCalibration() when done.
	 */
	@Override
	public void startCalibration() {
		buf[0] = BEGIN_CALIBRATION; 
		sendData(COMMAND, buf, 1);
	}
	
	/**
	 * Ends calibration sequence.
	 *
	 */	
	@Override
	public void stopCalibration() {
		buf[0] = END_CALIBRATION;
		sendData(COMMAND, buf, 1);
	}
}
