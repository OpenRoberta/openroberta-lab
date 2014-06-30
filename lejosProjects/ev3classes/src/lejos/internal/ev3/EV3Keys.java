package lejos.internal.ev3;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.internal.io.NativeDevice;
import lejos.internal.io.SystemSettings;
import lejos.utility.Delay;

public class EV3Keys implements Keys {

	private static final int PRESS_EVENT_SHIFT = 0;
	private static final int RELEASE_EVENT_SHIFT = 8;
	private static final int WAITFOR_RELEASE_SHIFT = 8;
	
	private Map<Integer,EV3Key> listeners;

	private static final int DEBOUNCE_TIME = 10;
	private static final int POLL_TIME = 50;

	private static int simulatedState;

	// protected by monitor
	private int clickVol;
	private int clickLen;
	private int clickFreq = 1000;
	private int curButtonsS;
	// not protected by any monitor
	private int curButtonsE;

	private NativeDevice dev;
	private ByteBuffer buttonState;

	public EV3Keys() {
		clickVol = SystemSettings.getIntSetting(VOL_SETTING, 20);
		clickLen = SystemSettings.getIntSetting(LEN_SETTING, 50);
		clickFreq = SystemSettings.getIntSetting(FREQ_SETTING, 1000);
		dev = new NativeDevice("/dev/lms_ui");
		buttonState = dev.mmap(6).getByteBuffer(0, 6);
		curButtonsE = curButtonsS = getButtons();
	}

	@Override
	public void discardEvents() {
		curButtonsE = getButtons();
	}

	@Override
	public int waitForAnyEvent() {
		return waitForAnyEvent(0);
	}

	@Override
	public int waitForAnyEvent(int timeout) {
		long end = (timeout == 0 ? 0x7fffffffffffffffL : System
				.currentTimeMillis() + timeout);
		try {
			int oldDown = curButtonsE;
			while (true) {
				long curTime = System.currentTimeMillis();
				if (curTime >= end)
					return 0;
				Thread.sleep(POLL_TIME);
				int newDown = curButtonsE = readButtons();
				if (newDown != oldDown)
				{
	                Thread.sleep(POLL_TIME);
	                newDown |= readButtons();
	                curButtonsE = newDown;
					return ((oldDown & (~newDown)) << WAITFOR_RELEASE_SHIFT)
							| (newDown & (~oldDown));
				}
			}
		} catch (InterruptedException e) {
			// TODO: Need to decide how to handle this properly
			// preserve state of interrupt flag
			Thread.currentThread().interrupt();
			return 0;
		} finally {
		}
	}

	@Override
	public int waitForAnyPress(int timeout) {
		long end = (timeout == 0 ? 0x7fffffffffffffffL : System
				.currentTimeMillis() + timeout);
		try {
			int oldDown = curButtonsE;
			while (true) {
				long curTime = System.currentTimeMillis();
				if (curTime >= end)
					return 0;
				Thread.sleep(POLL_TIME);
				int newDown = curButtonsE = readButtons();
				int pressed = newDown & (~oldDown);
				if (pressed != 0)
					return pressed;

				oldDown = newDown;
			}
		} catch (InterruptedException e) {
			// TODO: Need to decide how to handle this properly
			// preserve state of interrupt flag
			Thread.currentThread().interrupt();
			return 0;
		} finally {
		}
	}

	@Override
	public int waitForAnyPress() {
		return waitForAnyPress(0);
	}

	@Override
	public int getButtons() {
		// read buttons and de-bounce them
		int state1, state2 = 0;
		for (;;) {
			state1 = checkButtons();
			if (state1 == state2)
				return state1 | simulatedState;
			Delay.msDelay(DEBOUNCE_TIME);
			state2 = checkButtons();
		}
	}

	@Override
	public int readButtons() {
		int newButtons = getButtons();
		int pressed = newButtons & (~curButtonsS);
		curButtonsS = newButtons;
		if (pressed != 0 && clickVol != 0) {
			int tone = clickFreq;
			if (tone != 0)
				Sound.playTone(tone, clickLen, -clickVol);
		}
		return newButtons;
	}

	@Override
	public void setKeyClickVolume(int vol) {
		clickVol = vol;
	}

	@Override
	public int getKeyClickVolume() {
		return clickVol;
	}

	@Override
	public void setKeyClickLength(int len) {
		clickLen = len;
	}

	@Override
	public int getKeyClickLength() {
		return clickLen;
	}

	@Override
	public void setKeyClickTone(int key, int freq) {
		clickFreq = freq;
	}

	@Override
	public int getKeyClickTone(int key) {
		return clickFreq;
	}

	private int checkButtons() {
		int state = 0;
		for (int i = 0; i < buttonState.capacity(); i++)
			if (buttonState.get(i) != 0)
				state |= 1 << i;
		return state;
	}
	
	public void setSimultedState(int state) {
		simulatedState = state;
	}

	public int getSimulatedState() {
		return simulatedState;
	}
	
	void addListener(int iCode,EV3Key key) {
		if (listeners == null) {
			listeners = new HashMap<Integer,EV3Key>();
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
				int state = EV3Keys.this.waitForAnyEvent();
				
				int mask  = 1;
				for (int i=0;i<NUM_KEYS;i++) {
					if ((state & (mask << PRESS_EVENT_SHIFT))  != 0 || (state & (mask << RELEASE_EVENT_SHIFT)) != 0) {;
						EV3Key key = listeners.get(mask);
						if (key != null) key.callListeners();
					}
					mask <<= 1;
				}
			}
		}
		
	}
}




