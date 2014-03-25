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
public class MindsensorsLineLeader extends I2CSensor implements SensorMode {
	private byte[] buf = new byte[8];
	private final static byte COMMAND = 0x41;

    /**
     * Constructor
     *
     * @param port
     * @param address I2C address for the device
     */
    public MindsensorsLineLeader(I2CPort port, int address) {
        super(port, address);
        init();
    }

    /**
     * Constructor
     *
     * @param port
     */
    public MindsensorsLineLeader(I2CPort port) {
        this(port, DEFAULT_I2C_ADDRESS);
        init();
    }
    
    /**
     * Constructor
     *
     * @param port
     * @param address I2C address for the device
     */
    public MindsensorsLineLeader(Port port, int address) {
        super(port, address, TYPE_LOWSPEED_9V);
        init();
    }

    /**
     * Constructor
     *
     * @param port
     */
    public MindsensorsLineLeader(Port port) {
        this(port, DEFAULT_I2C_ADDRESS);
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
		getData(0x49,buf,8);
		
		for(int i=0;i<8;i++) {
			sample[offset+i] = buf[i];
		}	
	}

	@Override
	public String getName() {
		return "Light";
	}
}
