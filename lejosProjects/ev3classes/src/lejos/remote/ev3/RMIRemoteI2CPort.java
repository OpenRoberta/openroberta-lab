package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.I2CPort;
import lejos.remote.ev3.RMII2CPort;

public class RMIRemoteI2CPort extends UnicastRemoteObject implements RMII2CPort {
	private I2CPort port;
	
	private static final long serialVersionUID = 3049365457299818710L;

	protected RMIRemoteI2CPort(String portName) throws RemoteException {
		super(0);
		port = LocalEV3.get().getPort(portName).open(I2CPort.class);
	}
	
	@Override
	public void close() {
		port.close();
	}

	@Override
	public byte[] i2cTransaction(int deviceAddress, byte[] writeBuf,
			int writeOffset, int writeLen, int readLen) throws RemoteException {
		byte[] readBuf = new byte[readLen];
		port.i2cTransaction(deviceAddress, writeBuf, writeOffset, writeLen, readBuf, 0, readLen);
		return readBuf;
	}

	@Override
	public boolean setType(int type) throws RemoteException {
		return port.setType(type);
	}
}
