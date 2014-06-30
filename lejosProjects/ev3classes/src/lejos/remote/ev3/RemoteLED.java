package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.LED;
import lejos.hardware.port.PortException;

public class RemoteLED implements LED {
	private RMILED led;
	
	public RemoteLED(RMILED led) {
		this.led=led;
	}
	
	@Override
	public void setPattern(int pattern) {
		try {
			led.setPattern(pattern);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}
}
