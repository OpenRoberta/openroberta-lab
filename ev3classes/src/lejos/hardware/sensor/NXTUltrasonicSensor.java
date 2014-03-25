package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/** Represent the UltraSonic sensor for the NXT. <br> 
 * The sensor has two active modes. In continuous mode the sensor periodically scans the surrounding and measures the distance to the nearest object in sight.
 * In ping mode the sensor only scans the surrounding when a sample is fetched. It will then retun the distance of the eight nearest objects in sight. 
 * @author Aswin
 *
 */
public class NXTUltrasonicSensor extends I2CSensor  {
	// Supported modes
	// According to the datasheet there are other modes. These modes do not function on the sensor and are not implemented;
	protected static final byte MODE_OFF = 0x00;
	protected static final byte MODE_PING = 0x01;
	protected static final byte MODE_CONTINUOUS = 0x02;
	
	// Registers
	private static final byte REG_MODE = 0x41;
	private static final byte REG_DISTANCE = 0x42;

	// Timing values for the sensor
	private static final int DELAY_CMD = 5;
	private static final int DELAY_DATA_PING = 50;
	private static final int DELAY_DATA_CONTINUOUS = 30;

	//multiplication factor to convert to SI unit meter.
	private static final float TOSI=0.01f; 

	private byte currentMode=0;
	private byte[] byteBuff = new byte[8];
	private long nextCmdTime = 0;
	private long dataAvailableTime;

	
	/**
	 * Wait until the specified time
	 */
	private void waitUntil(long when)
	{
		long delay = when - System.currentTimeMillis();
		Delay.msDelay(delay);
	}

	/*
	 * Over-ride standard get function to ensure correct inter-command timing
	 * when using the ultrasonic sensor. The Lego Ultrasonic sensor uses a
	 * "bit-banged" i2c interface and seems to require a minimum delay between
	 * commands otherwise the commands fail.
	 */
	@Override
	public synchronized void getData(int register, byte[] buf, int off, int len)
	{
		waitUntil(nextCmdTime);
		super.getData(register, buf, off, len);
		nextCmdTime = System.currentTimeMillis() + DELAY_CMD;
	}

	/*
	 * Over-ride the standard send function to ensure the correct inter-command
	 * timing for the ultrasonic sensor.
	 */
	@Override
	public synchronized void sendData(int register, byte[] buf, int off, int len)
	{
		waitUntil(nextCmdTime);
		super.sendData(register, buf, off, len);
		nextCmdTime = System.currentTimeMillis() + DELAY_CMD;
	}



	public NXTUltrasonicSensor(I2CPort port) {
		super(port);
		init();
	}
	
	public NXTUltrasonicSensor(Port port) {
		super(port, DEFAULT_I2C_ADDRESS, TYPE_LOWSPEED_9V);
		init();
	}
	
	private void init() {
	  setModes(new SensorMode[]{ new ContinuousMode(), new PingMode()}); 
		nextCmdTime = System.currentTimeMillis() + DELAY_CMD;
		setMode(MODE_CONTINUOUS);		
		dataAvailableTime = System.currentTimeMillis()+DELAY_DATA_CONTINUOUS; 
		
	}

	/** Gives a SampleProvider representing the sensor in continuous mode. 
	 * In this mode the sensor scans the surrounding continuously and reports the distance to the nearest object in sight.
	 * The sensor reports the distance to the nearest object in meters. The theoretical range of the sensor is 0,04 to 2.54 meter.
	 * If there is no object detected within this range the sensor reports Float.POSITIVE_INFINITY. <br>
	 * Samples can only be provided every 30 ms. If samples are fetched more often the object will pause until the 30ms have passed.
	 * @return
	 * A SamplePrivider
	 */
	public SampleProvider getContinuousMode() {
	  return getMode(0);
	}

	
	/** Gives a SampleProvider representing the sensor in ping mode. 
	 * In this mode the sensor only scans the surrounding upon request, ie by calling fetchSample method. 
	 * The sensor reports the distance, expressed in meters, to the eight nearest objects in sight. The theoretical range of the sensor is 0,04 to 2.54 meter.
	 * If there are less then eight objects within the sensors range the sensor will reports Float.POSITIVE_INFINITY. 
	 * Fetching a sample in ping mode takes about 70 ms. 
	 * @return
	 */
	public SampleProvider getPingMode() {
    return getMode(0);
	}
	
	public void enable() {
		setMode(MODE_CONTINUOUS);
	}
	
	public void disable() {
		setMode(MODE_OFF);
	}
	
	public boolean isEnabled() {
		return (currentMode==MODE_OFF) ? false : true;
	}
	
	/** Sets the sensor to CONTINUOUS, PING or OFF
	 * @param mode
	 * @return
	 * True, if the mode was changed. <br>
	 * False, if the mode was already in the requested state.
	 */
	protected boolean setMode(byte mode) {
		if (mode !=currentMode) {
			byteBuff[0]=mode;
			 sendData(REG_MODE, byteBuff, 1);
			 currentMode=mode;
			 return true;
		}
		return false;
	}

	
	public class ContinuousMode implements SampleProvider, SensorMode {

		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			if (setMode(MODE_CONTINUOUS)) {				
				dataAvailableTime = System.currentTimeMillis()+DELAY_DATA_CONTINUOUS; // when the sensor was in another mode, the wait period just start. Otherwise it was set the last time this method was called;
			}
			waitUntil(dataAvailableTime); 
			getData(REG_DISTANCE, byteBuff, 1);
			int raw=byteBuff[0] & 0xff;
			
			sample[offset]= (raw==255) ? Float.POSITIVE_INFINITY : raw*TOSI;
			
			dataAvailableTime = System.currentTimeMillis()+DELAY_DATA_CONTINUOUS; 
		}

    @Override
    public String getName() {
      return "Distance";
    }

	}
	
	public class PingMode implements SampleProvider, SensorMode {

		@Override
		public int sampleSize() {
			return 8;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			setMode(MODE_PING);
			Delay.msDelay(DELAY_DATA_PING);
			getData(REG_DISTANCE, byteBuff, 8);
			for (int i = 0; i < 8;i++) {
				int raw=byteBuff[i] & 0xff;
				sample[i+offset]= (raw==255) ? Float.POSITIVE_INFINITY : raw*TOSI;
			}
			currentMode=MODE_OFF;
		}

    @Override
    public String getName() {
      return "Distances";
    }
	}

}
