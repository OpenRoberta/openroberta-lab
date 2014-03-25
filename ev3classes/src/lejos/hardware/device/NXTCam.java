package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

import java.awt.*;

/**
 * Mindsensors NXTCam.
 * www.mindsensors.com
 * 
 * Author Lawrie Griffiths
 * 
 */
public class NXTCam extends I2CSensor {
	byte[] buf = new byte[4];
	
	/**
	 * Used by sortBy() to choose sorting criteria based on size (ordered largest to smallest).
	 */
	public static final char SIZE = 'A';
	
	/**
	 * Used by sortBy() to choose sorting criteria based on color id (ordered 0 to 7).
	 */
	public static final char COLOR = 'U';
	
	/**
	 * Used by sortBy() to choose no sorting of detected objects.
	 */
	public static final char NO_SORTING = 'X';
	
	/**
	 * Used by setTrackingMode() to choose object tracking.
	 */
	public static final char OBJECT_TRACKING = 'B';
	
	/**
	 * Used by setTrackingMode() to choose line tracking.
	 */
	public static final char LINE_TRACKING = 'L';
	
	public NXTCam(I2CPort port, int address)
	{
		super(port, address);
	}

	public NXTCam(I2CPort port)
	{
		this(port, DEFAULT_I2C_ADDRESS);
	}

    public NXTCam(Port port, int address)
    {
        super(port, address, TYPE_LOWSPEED_9V);
    }
    
    public NXTCam(Port port)
    {
        this(port, DEFAULT_I2C_ADDRESS);
    }

	/**
	 * Get the number of objects being tracked
	 * 
	 * @return number of objects (0 - 8)
	 */
	public int getNumberOfObjects() {
		getData(0x42, buf, 1);
		return (0xFF & buf[0]);
	}
	
	/**
	 * Camera sorts objects it detects according to criteria, either color, size,
	 * or no sorting at all.
	 * @param sortType Use the class constants SIZE, COLOR, or NO_SORTING.
	 */
	public void sortBy(char sortType) {
		sendCommand(sortType);
	}
	
	/**
	 * 
	 * @param enable true to enable, false to disable
	 */
	public void enableTracking(boolean enable) {
		if(enable) sendCommand('E');
		else sendCommand('D');
	}
	
	/**
	 * Choose either object or line tracking mode.
	 * @param mode Use either OBJECT_TRACKING or LINE_TRACKING
	 */
	public void setTrackingMode(char mode) {
		sendCommand(mode);
	}
	
	/**
	 * Get the color number for a tracked object
	 * 
	 * @param id the object number (starting at zero)
	 * @return the color of the object (starting at zero)
	 */
	public int getObjectColor(int id) {
		getData(0x43 + (id * 5), buf, 1);
		return (0xFF & buf[0]);
	}
	
	/**
	 * Returns the NXTCam firmware version.
	 * @return version number as a string
	 */
	public String getFirmwareVersion() {
		sendCommand('V');
		byte mem_loc = 0x42;
		// NXTCam V1.1 appears to need a delay here otherwise it doesn't refresh the memory 
		// at location 0x42 the first time. 50 ms is not enough, 100 ms works:
		try {  
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return fetchString(mem_loc, 12);
	}
	
	/**
	 * Get the rectangle containing a tracked object
	 * 
	 * @param id the object number (starting at zero)
	 * @return the rectangle
	 */
	public Rectangle getRectangle(int id) {
		for(int i=0;i<4;i++) buf[i] = 0;
		getData(0x44 + (id * 5), buf, 4);
		return new Rectangle(buf[0] & 0xFF, buf[1] & 0xFF,
				(buf[2] & 0xFF) - (buf[0] & 0xFF),
				(buf[3] & 0xFF) - (buf[1] & 0xFF));
	}
	
	/**
	 * Send a single byte command represented by a letter
	 * @param cmd the letter that identifies the command
	 */
	public void sendCommand(char cmd) {
		sendData(0x41, (byte) cmd);
	}
}

