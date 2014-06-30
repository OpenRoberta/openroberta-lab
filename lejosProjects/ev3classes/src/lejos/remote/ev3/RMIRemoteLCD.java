package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.lcd.LCD;

public class RMIRemoteLCD extends UnicastRemoteObject implements RMILCD {
	private static final long serialVersionUID = 2378483122054140698L;

	protected RMIRemoteLCD() throws RemoteException {
		super(0);
	}

	@Override
	public void drawChar(char c, int x, int y) throws RemoteException {
		LCD.drawChar(c, x, y);
	}

	@Override
	public void clearDisplay() throws RemoteException {
		LCD.clearDisplay();
	}

	@Override
	public void drawString(String str, int x, int y, boolean inverted) {
		LCD.drawString(str, x, y, inverted);	
	}

	@Override
	public void drawString(String str, int x, int y) throws RemoteException {
		LCD.drawString(str, x, y);	
	}

	@Override
	public void drawInt(int i, int x, int y) throws RemoteException {
		LCD.drawInt(i, x, y);
	}

	@Override
	public void drawInt(int i, int places, int x, int y) throws RemoteException {
		LCD.drawInt(i, places, x, y);
	}
}
