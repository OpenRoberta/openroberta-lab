package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.Calibrate;

/**
 * This class supports the <a href="http://mindsensors.com">Mindsensors</a> compass sensor.
 * 
 * See http://www.mindsensors.com/index.php?module=pagemaster&PAGE_user_op=view_page&PAGE_id=56
 * 
 * author Lawrie Griffiths
 * 
 */
public class MindsensorsCompass extends I2CSensor implements Calibrate, SensorMode {
	private final static byte COMMAND = 0x41;
	private final static byte BEGIN_CALIBRATION = 0x43;
	private final static byte END_CALIBRATION = 0x44;
	
	private byte[] buf = new byte[2];
	
    /**
     * Create a compass sensor object
     * @param port I2C port for the compass
     * @param address The I2C address used by the sensor
     */
    public MindsensorsCompass(I2CPort port, int address)
    {
		super(port, address);
		init();
    }
	
	/**
	 * Create a compass sensor object
	 * @param port I2C port for the compass
	 */
	public MindsensorsCompass(I2CPort port) {
		super(port, DEFAULT_I2C_ADDRESS);
		init();
	}
	
    /**
     * Create a compass sensor object
     * @param port Sensor port for the compass
     * @param address The I2C address used by the sensor
     */
    public MindsensorsCompass(Port port, int address)
    {
        super(port, address);
        init();
    }
    
	/**
	 * Create a compass sensor object
	 * @param port Sensor port for the compass
	 */
	public MindsensorsCompass(Port port) {
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
		getData(0x42, buf, 1);
		
		// TODO: Could use integer mode for higher resolution
		sample[offset]=(255-(buf[0] & 0xFF))*359f/255f;
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
	public void startCalibration() {
		buf[0] = BEGIN_CALIBRATION; 
		sendData(COMMAND, buf, 1);
	}
	
	/**
	 * Ends calibration sequence.
	 *
	 */
	public void stopCalibration() {
		buf[0] = END_CALIBRATION;
		sendData(COMMAND, buf, 1);
	}
}
