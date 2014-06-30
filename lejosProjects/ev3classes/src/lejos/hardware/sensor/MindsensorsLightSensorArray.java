package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;

/**
 * This class manages the sensor NXT Line Leader from Mindsensors. The sensor
 * add a sensor row to detect black/white lines.
 * 
 * This sensor is perfect to build a robot which has the mission to follow a
 * line.
 * 
 * @author Juan Antonio Brenha Moral
 * @author Eric Pascual (EP)
 */
public class MindsensorsLightSensorArray extends I2CSensor implements SensorMode {
	private byte[] buf = new byte[8];
	private final static byte COMMAND = 0x41;
	private final static byte DATA = 0x42;
	private final static int FACTORY_DEFAULT = 0x14;

    /**
     * Constructor
     *
     * @param port
     * @param address I2C address for the device
     */
    public MindsensorsLightSensorArray(I2CPort port, int address) {
        super(port, address);
        init();
    }

    /**
     * Constructor
     *
     * @param port
     */
    public MindsensorsLightSensorArray(I2CPort port) {
        this(port, FACTORY_DEFAULT);
        init();
    }
    
    /**
     * Constructor
     *
     * @param port
     * @param address I2C address for the device
     */
    public MindsensorsLightSensorArray(Port port, int address) {
        super(port, address, TYPE_LOWSPEED);
        init();
    }

    /**
     * Constructor
     *
     * @param port
     */
    public MindsensorsLightSensorArray(Port port) {
        this(port, FACTORY_DEFAULT);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    }
    
	/**
	 * Send a single byte command represented by a letter
	 * 
	 * @param cmd the command to be sent
	 */
	public void sendCommand(char cmd) {
		sendData(COMMAND, (byte) cmd);
	}

	/**
	 * Sleep the sensor
	 */
	public void sleep() {
		sendCommand('D');
	}

	/**
	 * Wake up the sensor
	 * 
	 */
	public void wakeUp() {
		sendCommand('P');
	}
	
	public void calibrateWhite() {
		sendCommand('W');
	}
	
	public void calibrateBlack() {
		sendCommand('B');
	}
	
	/**
	 * Return a sample provider in light mode
	 */
	public SensorMode getLightMode() {
		return this;
	}

	@Override
	public int sampleSize() {
		return 8;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(DATA,buf,8);
		
		for(int i=0;i<8;i++) {
			sample[offset+i] = buf[i];
		}	
	}

	@Override
	public String getName() {
		return "Light";
	}
}
