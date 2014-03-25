package lejos.remote.nxt;

import java.io.IOException;

import lejos.hardware.port.I2CException;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.PortException;

public class RemoteNXTI2CPort extends RemoteNXTIOPort implements I2CPort
{
    private int type;
    private int mode;
    private int id;
	
    public RemoteNXTI2CPort(NXTCommand nxtCommand) {
		super(nxtCommand);
	}

    protected boolean getChanged()
    {
        return false;
    }
    
    protected byte getStatus()
    {
        return 0;
    }

    protected void reset()
    {
    	// Do nothing
    }

    protected void setOperatingMode(int typ, int mode)
    {
    	// Do nothing
    }
    
    protected boolean initSensor()
    {
        return true;
    }
    
    /**
     * allow access to the specified port
     * @param p port number to open
     */
    public boolean open(int t, int p, RemoteNXTPort r)
    {
        if (!super.open(t, p, r))
            return false;
        if (!initSensor())
        {
            super.close();
            return false;
        }
        return true;
    }
    
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
    public synchronized void i2cTransaction(int deviceAddress, byte[]writeBuf,
            int writeOffset, int writeLen, byte[] readBuf, int readOffset,
            int readLen)
    {
    	System.out.println("Remote I2C transaction on port: " + port + " , address: " + deviceAddress);
		byte [] txData = new byte[writeLen + 1];
	    txData[0] =(byte) deviceAddress;
	    System.arraycopy(writeBuf, writeOffset, txData, 1, writeLen);
		int status;
		try {
			nxtCommand.LSWrite((byte) port, txData, (byte) readLen);
		} catch (IOException e) {
			throw new PortException(e);
		}
		
		do {
			try {
				byte[] ret = nxtCommand.LSGetStatus((byte) port);
				if (ret == null || ret.length < 1) throw new I2CException("Remote NXT I2C LSGetStatus error");
				status = (int) ret[0];
			} catch (IOException e) {
				throw new PortException(e);
			}
			
		} while (status == ErrorMessages.PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS || 
				 status == ErrorMessages.SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY);

		try {
			byte [] ret = nxtCommand.LSRead((byte) port);
            if (ret == null) throw new I2CException("Remote NXT I2C LSRead error");
            if (readLen != ret.length) throw new I2CException("Remote NXT I2C wrong number of bytes read");
            if (readLen > 0)
                System.arraycopy(ret, 0, readBuf, readOffset, readLen);
		} catch (IOException e) {
			throw new PortException(e);
		}
    } 
    
    /**
     * Set the sensor type and mode
     * @param type the sensor type
     * @param mode the sensor mode
     * @return 
     */
    public boolean setTypeAndMode(int type, int mode) {
        this.type = type;
        this.mode = mode;
        try {
            nxtCommand.setInputMode(id, type, mode);
        } catch (IOException e) {
            throw new PortException(e);
        }
        return true;
    }
    
    /**
     * Set the sensor type
     * @param type the sensor type
     * @return 
     */
    public boolean setType(int type) {
        this.type = type;
        setTypeAndMode(type, mode);
        return true;
    }
    
    /**
     * Set the sensor mode
     * @param mode the sensor mode
     * @return 
     */
    public boolean setMode(int mode) {
        this.mode = mode;
        setTypeAndMode(type, mode);
        return true;
    }

}
