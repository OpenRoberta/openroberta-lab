package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.port.PortException;
import lejos.hardware.port.UARTPort;

public class RemoteUARTPort extends RemoteIOPort implements UARTPort {
	protected RMIUARTPort rmi;
	protected RMIEV3 rmiEV3;
	
	public RemoteUARTPort(RMIEV3 rmiEV3) {
		this.rmiEV3 = rmiEV3;
	}
	
	public boolean open(int typ, int portNum, RemotePort remotePort) {
        boolean res = super.open(typ,portNum,remotePort);
		try {
			rmi = rmiEV3.openUARTPort(getName());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
		return res;
	}
	
	@Override
	public void close() {
		super.close();
		try {
			rmi.close();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	@Override
	public byte getByte() {
		try {
			return rmi.getByte();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void getBytes(byte[] vals, int offset, int len) {
		try {
			rmi.getBytes(vals, offset, len);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getShort() {
		try {
			return rmi.getShort();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void getShorts(short[] vals, int offset, int len) {
		try {
			rmi.getShorts(vals, offset, len);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public String getModeName(int mode) {
		try {
			return rmi.getModeName(mode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public boolean initialiseSensor(int mode) {
		try {
			return rmi.initialiseSensor(mode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void resetSensor() {
		try {
			rmi.resetSensor();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	@Override
	public String toString() {
		try {
			return rmi.toStringValue();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	@Override
	public boolean setMode(int mode) {
		try {
			return rmi.setMode(mode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
}
