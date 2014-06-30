package lejos.internal.ev3;

import java.util.ArrayList;

import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.utility.Delay;

public class EV3Key implements Key {	
	private static final int WAITFOR_RELEASE_SHIFT = 8;
	private static final int DEBOUNCE_TIME = 10;
	private static final int KEY_PRESS_TIME = 100;
	
	private int iCode;
	private EV3Keys keys;
	
	private static String[] keyNames = {"Up", "Enter", "Down", "Right", "Left", "Escape"};
	private String name;
	
	private ArrayList<KeyListener> listeners;
	
	public EV3Key(EV3Keys keys, String name) {
		this.iCode = getKeyId(name);
		this.keys = keys;
		this.name = name;
	}

	@Override
	public int getId() {
		return iCode;
	}

	@Override
	public boolean isDown() {
		return (keys.readButtons() & iCode) != 0;
	}

	@Override
	public boolean isUp() {
		return (keys.readButtons() & iCode) == 0;
	}

	@Override
	public void waitForPress() {
		while ((keys.waitForAnyPress(0) & iCode) == 0)
		{
			// wait for next press
		}
	}

	@Override
	public void waitForPressAndRelease() {
		this.waitForPress();
		int tmp = iCode << WAITFOR_RELEASE_SHIFT;
		while ((keys.waitForAnyEvent(0) & tmp) == 0)
		{
			// wait for next event
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
		int simulatedState = keys.getSimulatedState();
		if (event == Key.KEY_PRESSED) keys.setSimultedState(simulatedState |= iCode); // set bit
		else if (event == Key.KEY_RELEASED) keys.setSimultedState(simulatedState &= ~iCode); // unset bit
		else if (event == Key.KEY_PRESSED_AND_RELEASED) {
			keys.setSimultedState(simulatedState |= iCode);
			Delay.msDelay(KEY_PRESS_TIME);
			keys.setSimultedState(simulatedState &= ~iCode);
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
	
	public static int getKeyPos(String name) {
		for(int i=0;i<keyNames.length;i++) {
			if (name.equals(keyNames[i])) return i;
		}
		return -1;
	}
	
	public static int getKeyId(String name) {
		return 1 << getKeyPos(name);
	}

	@Override
	public String getName() {
		return name;
	}
}
