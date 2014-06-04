package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import lejos.utility.EndianTools;

/**
 * This Class manages the Micro Infinity Cruizcore XG1300L
 * 
 * @author Daniele Benedettelli, February 2011
 * @version 1.0
 */
public class CruizcoreGyro extends I2CSensor implements SensorMode {

	/*
	 * Documentation can be obtained here: http://www.minfinity.com/Manual/CruizCore_XG1300L_User_Manual.pdf
	 * The documentation and the conversion in the NXC sample code indicate,
	 * that 16bit signed little endian values are returned.
	 */

	private byte[] inBuf = new byte[11];
	private static final byte GYRO_ADDRESS = 0x02;
	
	// values returned are signed short integers multiplied by 100
	private static final byte ANGLE = 0x42; // 0x43 (2 Bytes)
	
	private static final byte RATE = 0x44; // 0x45 (2 Bytes)
	
	private static final byte ACCEL_X = 0x46; // 0x47 (2 Bytes)
//	private static final byte ACCEL_Y = 0x48; // 0x49 (2 Bytes)
//	private static final byte ACCEL_Z = 0x4A; // 0x4B (2 Bytes)
	
	// the commands are issued by just reading these registers 

	private static final byte RESET = 0x60;

	private static final byte SELECT_SCALE = 0x61;
	
		
	private float scale=1;
	
	/**
	 * Instantiates a new Cruizcore Gyro sensor.
	 *
	 * @param port the port the sensor is attached to
	 */
    public CruizcoreGyro(I2CPort port) {
        super(port, GYRO_ADDRESS);
        init();
    }
    
    public CruizcoreGyro(Port port) {
        super(port, GYRO_ADDRESS);
        init();
    }
    
    
    protected void init() {
      setAccScale2G();
    	setModes(new SensorMode[]{ this, new RateMode(),  new AngleMode() });
    }
    
 
	
	/**
	 * Sets the acc scale.
	 *
	 * @param sf the scale factor 
	 * 0 for +/- 2G
	 * 1 for +/- 4G
	 * 2 for +/- 8g
	 */
	public void setAccScale(byte sf)
	{
		sendData(SELECT_SCALE + sf, (byte) 0);
		scale=(float) (9.81 / 1000f * Math.pow(2, sf + 1) / 2);
	}
	
	/**
	 * Sets the acceleration scale factor to 2G.
	 */
	public void setAccScale2G()
	{
	  setAccScale((byte) 0);
	}	

	/**
	 * Sets the acceleration scale factor to 4G.
	 */
	public void setAccScale4G()
	{
    setAccScale((byte) 1);
	}	
	
	/**
	 * Sets the acceleration scale factor to 8G.
	 */
	public void setAccScale8G()
	{
    setAccScale((byte) 2);
	}		
	
	/**
	 * Resets the accumulated angle (heading).
	 *
	 */
	public void reset() {
		sendData(RESET, (byte)0);
		Delay.msDelay(750);		
	}

	@Override
	public int sampleSize() {
		return 3;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(ACCEL_X,inBuf,6);
		sample[0+offset] = EndianTools.decodeShortLE(inBuf, 2) * scale;
		sample[1+offset] = EndianTools.decodeShortLE(inBuf, 0) * scale;
		sample[2+offset] = - EndianTools.decodeShortLE(inBuf, 4) * scale;		
	}
	
	@Override
	public String getName() {
		return "Acceleration";
	}
	
	public SensorMode getAccelerationMode() {
		return this;
	}
	
	public SensorMode getRateMode() {
		return getMode(1);
	}
	
	private class RateMode implements SensorMode {
		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			getData(RATE,inBuf,2);
			sample[offset] = -EndianTools.decodeShortLE(inBuf, 0) /100f;		
		}

		@Override
		public String getName() {
			return "Rate";
		}		
	}
	
	public SensorMode getAngleMode() {
		return getMode(2);
	}
	
	private class AngleMode implements SensorMode {
		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			getData(ANGLE,inBuf,2);
			sample[offset] = 360 - EndianTools.decodeShortLE(inBuf, 0)/100f;		
		}

		@Override
		public String getName() {
			return "Angle";
		}		
	}
}
