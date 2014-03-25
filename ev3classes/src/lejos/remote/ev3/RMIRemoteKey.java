package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.ev3.LocalEV3;

public class RMIRemoteKey  extends UnicastRemoteObject implements RMIKey {
	private Key key;
	private static final long serialVersionUID = -752148808854653746L;

	protected RMIRemoteKey() throws RemoteException {
		super(0);
	}

	@Override
	public int getId() throws RemoteException {
		return key.getId();
	}

	@Override
	public boolean isDown() throws RemoteException {
		return key.isDown();
	}

	@Override
	public boolean isUp() throws RemoteException {
		return key.isUp();
	}

	@Override
	public void waitForPress() throws RemoteException {
		key.waitForPress();
	}

	@Override
	public void waitForPressAndRelease() throws RemoteException {
		key.waitForPressAndRelease();
	}

	@Override
	public void addKeyListener(KeyListener listener) throws RemoteException {
		key.addKeyListener(listener);
	}

	@Override
	public void simulateEvent(int event) throws RemoteException {
		key.simulateEvent(event);
	}

	public void setId(String name) {
		key = LocalEV3.get().getKey(name);	
	}
}
