package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.Keys;
import lejos.hardware.ev3.LocalEV3;

public class RMIRemoteKeys extends UnicastRemoteObject implements RMIKeys  {
	private static final long serialVersionUID = -7696209202724226195L;
	private Keys keys = LocalEV3.get().getKeys();
	
	protected RMIRemoteKeys() throws RemoteException {
		super(0);
	}

	@Override
	public void discardEvents() throws RemoteException {
		keys.discardEvents();	
	}

	@Override
	public int waitForAnyEvent() throws RemoteException {
		return keys.waitForAnyEvent();
	}

	@Override
	public int waitForAnyEvent(int timeout) throws RemoteException {
		return keys.waitForAnyEvent(timeout);
	}

	@Override
	public int waitForAnyPress(int timeout) throws RemoteException {
		return keys.waitForAnyPress(timeout);
	}

	@Override
	public int waitForAnyPress() throws RemoteException {
		return keys.waitForAnyPress();
	}

	@Override
	public int getButtons() throws RemoteException {
		return keys.getButtons();
	}

	@Override
	public int readButtons() throws RemoteException {
		return keys.readButtons();
	}

	@Override
	public void setKeyClickVolume(int vol) throws RemoteException {
		keys.setKeyClickVolume(vol);	
	}

	@Override
	public int getKeyClickVolume() throws RemoteException {
		return keys.getKeyClickVolume();
	}

	@Override
	public void setKeyClickLength(int len) throws RemoteException {
		keys.setKeyClickLength(len);
	}

	@Override
	public int getKeyClickLength() throws RemoteException {
		return keys.getKeyClickLength();
	}

	@Override
	public void setKeyClickTone(int key, int freq) throws RemoteException {
		keys.setKeyClickTone(key, freq);	
	}

	@Override
	public int getKeyClickTone(int key) throws RemoteException {
		return keys.getKeyClickTone(key);
	}
}
