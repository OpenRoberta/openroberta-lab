package lejos.remote.ev3;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.remote.ev3.RMIBattery;

public class RMIRemoteBattery extends UnicastRemoteObject implements RMIBattery {

	protected RMIRemoteBattery() throws RemoteException {
		super(0);
	}

	@Override
	public int getVoltageMilliVolt() throws RemoteException {
		return LocalEV3.get().getPower().getVoltageMilliVolt();
	}

	@Override
	public float getVoltage() throws RemoteException {
		return LocalEV3.get().getPower().getVoltage();
	}

	@Override
	public float getBatteryCurrent() throws RemoteException {
		return LocalEV3.get().getPower().getBatteryCurrent();
	}

	@Override
	public float getMotorCurrent() throws RemoteException {
		return LocalEV3.get().getPower().getMotorCurrent();
	}

}
