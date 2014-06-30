package lejos.hardware.port;

import lejos.hardware.sensor.EV3SensorConstants;

/**
 * Abstraction for a port that supports I2C sensors.
 * 
 * @author Lawrie Griffiths
 *
 */
public interface I2CPort extends IOPort, BasicSensorPort {
	
    public static final int STANDARD_MODE = 0;
    public static final int LEGO_MODE = 1;
    public static final int ALWAYS_ACTIVE = 2;
    
    /** Do not release the i2c bus between requests */
    public static final int NO_RELEASE = 4;
    /** Use high speed I/O (125KHz) */
    public static final int HIGH_SPEED = 8;
    /** Maximum read/write request length */
    public static final int MAX_IO = EV3SensorConstants.IIC_DATA_LENGTH;
    
    /**
     * High level i2c interface. Perform a complete i2c transaction and return
     * the results. Writes the specified data to the device and then reads the
     * requested bytes from it.
     * @param deviceAddress The I2C device address.
     * @param writeBuf The buffer containing data to be written to the device.
     * @param writeOffset The offset of the data within the write buffer
     * @param writeLen The number of bytes to write.
     * @param readBuf The buffer to use for the transaction results
     * @param readOffset Location to write the results to
     * @param readLen The length of the read
     */
    public void i2cTransaction(int deviceAddress, byte[]writeBuf,
            int writeOffset, int writeLen, byte[] readBuf, int readOffset,
            int readLen);
}
