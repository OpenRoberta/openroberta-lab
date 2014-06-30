package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.util.ArrayList;

import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.port.PortException;
import lejos.internal.ev3.EV3Key;

public class RemoteKey implements Key {
	private RMIKey key;
	private RemoteKeys keys;
	private int iCode;
	private String name;
	
	private ArrayList<KeyListener> listeners;
	
	public RemoteKey(RMIKey key, RemoteKeys keys, String name) {
		this.keys = keys;
		this.iCode = EV3Key.getKeyId(name);
		this.key=key;
		this.name = name;
	}

	@Override
	public int getId() {
		try {
			return key.getId();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public boolean isDown() {
		try {
			return key.isDown();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public boolean isUp() {
		try {
			return key.isUp();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void waitForPress() {
		try {
			key.waitForPress();
		} catch (RemoteException e) {
			throw new PortException(e);
		}		
	}

	@Override
	public void waitForPressAndRelease() {
		try {
			key.waitForPressAndRelease();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void addKeyListener(KeyListener listener) {
	    if (listeners == null) {
	    	listeners = new ArrayList<KeyListener>();
		}
		listeners.add(listener);
		keys.addListener(iCode, this);
	}

	@Override
	public void simulateEvent(int event) {
		try {
			key.simulateEvent(event);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	public void callListeners() {
	    boolean pressed = isDown();
	    
	    if (listeners != null)
		    for(KeyListener listener: listeners)  {
		    	if (pressed) listener.keyPressed(this);
		    	else listener.keyReleased(this);
		    }
	}

	@Override
	public String getName() {
		return name;
	}
}
