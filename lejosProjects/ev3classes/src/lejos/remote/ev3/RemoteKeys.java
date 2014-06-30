package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.Keys;
import lejos.hardware.port.PortException;

public class RemoteKeys implements Keys {
	private RMIKeys keys;
	private Map<Integer,RemoteKey> listeners;
	
	private static final int PRESS_EVENT_SHIFT = 0;
	private static final int RELEASE_EVENT_SHIFT = 8;
	
	public RemoteKeys(RMIKeys keys) {
		this.keys=keys;
	}
	
	@Override
	public void discardEvents() {
		try {
			keys.discardEvents();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int waitForAnyEvent() {
		try {
			return keys.waitForAnyEvent();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int waitForAnyEvent(int timeout) {
		try {
			return keys.waitForAnyEvent(timeout);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int waitForAnyPress(int timeout) {
		try {
			return keys.waitForAnyPress(timeout);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int waitForAnyPress() {
		try {
			return keys.waitForAnyPress();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getButtons() {
		try {
			return keys.getButtons();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int readButtons() {
		try {
			return keys.readButtons();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setKeyClickVolume(int vol) {
		try {
			keys.setKeyClickVolume(vol);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getKeyClickVolume() {
		try {
			return keys.getKeyClickVolume();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setKeyClickLength(int len) {
		try {
			keys.setKeyClickLength(len);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public int getKeyClickLength() {
		try {
			return keys.getKeyClickLength();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setKeyClickTone(int key, int freq) {
		try {
			keys.setKeyClickTone(key, freq);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getKeyClickTone(int key) {
		try {
			return keys.getKeyClickTone(key);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	void addListener(int iCode,RemoteKey key) {
		if (listeners == null) {
			listeners = new HashMap<Integer,RemoteKey>();
			new KeysListenThread().start();
		}
		listeners.put(iCode, key);
	}
	
	class KeysListenThread extends Thread {
		
		public KeysListenThread() {
			setDaemon(true);
		}
		
		@Override
		public void run() {
			while (true) {
				int state = RemoteKeys.this.waitForAnyEvent();
				
				int mask  = 1;
				for (int i=0;i<NUM_KEYS;i++) {
					if ((state & (mask << PRESS_EVENT_SHIFT))  != 0 || (state & (mask << RELEASE_EVENT_SHIFT)) != 0) {;
						RemoteKey key = listeners.get(mask);
						if (key != null) key.callListeners();
					}
					mask <<= 1;
				}
			}
		}
		
	}
}
