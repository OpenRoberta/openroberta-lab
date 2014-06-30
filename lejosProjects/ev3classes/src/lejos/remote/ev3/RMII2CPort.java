package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMII2CPort extends Remote {

    /**
     * High level i2c interface. Perform a complete i2c transaction and return
     * the results. Writes the specified data to the device and then reads the
     * requested bytes from it.
     * @param deviceAddress The I2C device address.
     * @param writeBuf The buffer containing data to be written to the device.
     * @param writeOffset The offset of the data within the write buffer
     * @param writeLen The number of bytes to write.
     * @param readLen The length of the read
     * @return < 0 error otherwise the number of bytes read
     */
    public byte[] i2cTransaction(int deviceAddress, byte[]writeBuf,
            int writeOffset, int writeLen, 
            int readLen) throws RemoteException;
    
	
	public void close() throws RemoteException;
	
    boolean setType(int type) throws RemoteException;
}
