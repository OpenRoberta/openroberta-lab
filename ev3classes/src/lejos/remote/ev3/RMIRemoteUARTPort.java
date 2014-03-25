package lejos.remote.ev3;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.UARTPort;
import lejos.remote.ev3.RMIAnalogPort;
import lejos.remote.ev3.RMIUARTPort;


public class RMIRemoteUARTPort extends UnicastRemoteObject implements RMIUARTPort {
	private UARTPort port;
	
	private static final long serialVersionUID = 6528817103650932337L;

	protected RMIRemoteUARTPort(String portName) throws RemoteException {
		super(0);
		port = LocalEV3.get().getPort(portName).open(UARTPort.class);
	}

	@Override
	public byte getByte() throws RemoteException {
		return port.getByte();
	}

	@Override
	public void getBytes(byte[] vals, int offset, int len)
			throws RemoteException {
		port.getBytes(vals, offset, len);	
	}

	@Override
	public int getShort() throws RemoteException {
		return port.getShort();
	}

	@Override
	public void getShorts(short[] vals, int offset, int len)
			throws RemoteException {
		port.getShorts(vals, offset, len);
	}

	@Override
	public String getModeName(int mode) throws RemoteException {
		return port.getModeName(mode);
	}

	@Override
	public String toStringValue() throws RemoteException {
		return port.toString();
	}

	@Override
	public boolean initialiseSensor(int mode) throws RemoteException {
		return port.initialiseSensor(mode);
	}

	@Override
	public void resetSensor() throws RemoteException {
		port.resetSensor();
	}

	@Override
	public void close() throws RemoteException {
		port.close();
	}

	@Override
	public boolean setMode(int mode) throws RemoteException {
		return port.setMode(mode);
	}
}
