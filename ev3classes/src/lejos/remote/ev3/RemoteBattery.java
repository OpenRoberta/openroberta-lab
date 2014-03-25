package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.Power;
import lejos.hardware.port.PortException;

public class RemoteBattery implements Power {
	RMIBattery battery;
	
	public RemoteBattery(RMIBattery battery) {
		this.battery = battery;
	}
	
	@Override
	public int getVoltageMilliVolt() {
		try {
			return battery.getVoltageMilliVolt();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public float getVoltage() {
		try {
			return battery.getVoltage();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public float getBatteryCurrent() {
		try {
			return battery.getBatteryCurrent();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public float getMotorCurrent() {
		try {
			return battery.getMotorCurrent();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
}
