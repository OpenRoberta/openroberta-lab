package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;

/**
 * Class to access the HiTechnic NXT Acceleration / Tilt Sensor (NAC1040).
 * 
 * Some sensors seem to be badly calibrated, so 0 is not always level.
 *  	
 * Documentation: http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NAC1040
 * Some details from HTAC-driver.h from http://botbench.com/blog/robotc-driver-suite/
 * 
 * ProductId: "HITECHNC"
 * SensorType: "Accel."
 * (confirmed for version " V1.1")
 * 
 * @author Lawrie Griffiths
 * @author Michael Mirwaldt
 */
public class HiTechnicAccelerometer extends I2CSensor implements SensorMode {
	private static final int BASE_ACCEL = 0x42;	
	private static final int OFF_X_HIGH = 0x00;
	private static final int OFF_Y_HIGH = 0x01;
	private static final int OFF_Z_HIGH = 0x02;
	private static final int OFF_2BITS = 3;	
	private static final float TO_SI = 0.049033251f;
	
	private byte[] buf = new byte[6];
	
	/**
	 * Creates a SampleProvider for the HiTechnic Acceleration Sensor
	 * 
	 * @param port the I2C port
	 * @param address the I2C address of the sensor
	 */
	public HiTechnicAccelerometer(I2CPort port, int address) {
		super(port, address);
		init();
	}
	
	/**
	 * Creates a SampleProvider for the HiTechnic Acceleration Sensor
	 * 
	 * @param port the I2C port
	 */
	public HiTechnicAccelerometer(I2CPort port) {
		this(port, DEFAULT_I2C_ADDRESS);
		init();
	}
	
	/**
	 * Creates a SampleProvider for the HiTechnic Acceleration Sensor
	 * 
	 * @param port the sensor port
	 * @param address the I2C address of the sensor
	 */
	public HiTechnicAccelerometer(Port port, int address) {
		super(port, address, TYPE_LOWSPEED_9V);
		init();
	}
	
	/**
	 * Creates a SampleProvider for the HiTechnic Acceleration Sensor
	 * 
	 * @param port the I2C port
	 */
    public HiTechnicAccelerometer(Port port) {
        this(port, DEFAULT_I2C_ADDRESS);
        init();
    }
    
    protected void init() {
       	setModes(new SensorMode[]{ this});
       }
    
    /**
     * Get s sample provider in acceleration mode
     */
    public SensorMode getAccelerationMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 3;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(BASE_ACCEL, buf, 0, 6);
		
		sample[offset+0] = ((buf[OFF_X_HIGH] << 2) + (buf[OFF_X_HIGH + OFF_2BITS] & 0xFF)) * TO_SI;
		sample[offset+1] = ((buf[OFF_Y_HIGH] << 2) + (buf[OFF_Y_HIGH + OFF_2BITS] & 0xFF)) * TO_SI;
		sample[offset+2] = ((buf[OFF_Z_HIGH] << 2) + (buf[OFF_Z_HIGH + OFF_2BITS] & 0xFF)) * TO_SI;	
	}

	@Override
	public String getName() {
		return "Acceleration";
	}
}
