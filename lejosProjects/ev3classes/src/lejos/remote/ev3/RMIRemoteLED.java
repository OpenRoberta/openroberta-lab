package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;

public class RMIRemoteLED extends UnicastRemoteObject implements RMILED  {
	private static final long serialVersionUID = -660643720408840563L;
	private LED led = LocalEV3.get().getLED();

	protected RMIRemoteLED() throws RemoteException {
		super(0);
	}

	@Override
	public void setPattern(int pattern) throws RemoteException {
		led.setPattern(pattern);
	}
}
