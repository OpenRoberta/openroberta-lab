package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.EndianTools;


/**
 * <p>Class for controlling dGPS sensor from Dexter Industries. Documentation for this sensor
 * can be found at <a href="http://www.dexterindustries.com/download.html#dGPS">Dexter Industries</a>.</p>
 * 
 * <p>The sensor uses an integer-based representation of latitude and longitude values.
 * Assume that you want to convert the value of 77 degrees, 2 minutes and 54.79 seconds
 * to the integer-based representation. The integer value is computed as follows:
 * <code>R = 1000000 * (D + M / 60 + S / 3600)</code>
 * where <code>D=77</code>, <code>M=2</code>, and <code>S=54.79</code>.
 * For the given values, the formula yields the integer value 77048553.
 * Basically, this is equivalent to decimal degrees times a million.</p>
 * 
 * @author Mark Crosbie  <mark@mastincrosbie.com>
 * 22 January, 2011
 *
*/
public class DexterGPSSensor extends I2CSensor implements SensorMode {
	/*
	 * Documentation can be found here: http://www.dexterindustries.com/download.html#dGPS
	 */
	
	public static final byte DGPS_I2C_ADDR   = 0x06;      /*!< Barometric sensor device address */
	public static final byte DGPS_CMD_UTC    = 0x00;      /*!< Fetch UTC */
	public static final byte DGPS_CMD_STATUS = 0x01;      /*!< Status of satellite link: 0 no link, 1 link */
	public static final byte DGPS_CMD_LAT    = 0x02;      /*!< Fetch Latitude */
	public static final byte DGPS_CMD_LONG   = 0x04;      /*!< Fetch Longitude */
	public static final byte DGPS_CMD_VELO   = 0x06;      /*!< Fetch velocity in cm/s */
	public static final byte DGPS_CMD_HEAD   = 0x07;      /*!< Fetch heading in degrees */
	public static final byte DGPS_CMD_DIST   = 0x08;      /*!< Fetch distance to destination */
	public static final byte DGPS_CMD_ANGD   = 0x09;      /*!< Fetch angle to destination */
	public static final byte DGPS_CMD_ANGR   = 0x0A;      /*!< Fetch angle travelled since last request */
	public static final byte DGPS_CMD_SLAT   = 0x0B;      /*!< Set latitude of destination */
	public static final byte DGPS_CMD_SLONG  = 0x0C;      /*!< Set longitude of destination */
	
	private byte reply[] = new byte[4];
	
	/**
	* Constructor
	* @param i2cPort the i2c port the sensor is connected to
	*/
    public DexterGPSSensor(I2CPort i2cPort) {
        super(i2cPort, DGPS_I2C_ADDR);
        init();
    }
    
	/**
	* Constructor
	* @param sensorPort the sensor port the sensor is connected to
	*/
    public DexterGPSSensor(Port sensorPort) {
        super(sensorPort, DGPS_I2C_ADDR);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    }
    
    /**
	* Return status of link to the GPS satellites
	* LED on dGPS should light if satellite lock acquired
	* @return true if GPS link is up, else false
	*/
    public boolean linkStatus() {
    	this.getData(DGPS_CMD_STATUS, reply, 0, 1);
    	return (reply[0] == 1);
    }
    
    /**
     * Return a sample provider in GPS mode
     */
    public SensorMode getGPSMode() {
    	return this;
    }

	@Override
	public int sampleSize() {
		return 5;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
    	getData(DGPS_CMD_LAT, reply, 0, 4); 	
    	sample[0+offset] = EndianTools.decodeIntBE(reply, 0) / 1000000f;
    	
    	getData(DGPS_CMD_LONG, reply, 0, 4);
    	sample[1+offset] = EndianTools.decodeIntBE(reply, 0) / 1000000f;
    	
    	getData(DGPS_CMD_HEAD, reply, 0, 2);
    	sample[2+offset] = EndianTools.decodeUShortBE(reply, 0);
    	
    	getData(DGPS_CMD_VELO, reply, 1, 3);
    	reply[0] = 0;
    	sample[3+offset] =  EndianTools.decodeIntBE(reply, 0) / 10f;
  
    	getData(DGPS_CMD_UTC, reply, 0, 4);
    	sample[4+offset] = EndianTools.decodeIntBE(reply, 0);
	}

	@Override
	public String getName() {
		return "GPS";
	}
}
