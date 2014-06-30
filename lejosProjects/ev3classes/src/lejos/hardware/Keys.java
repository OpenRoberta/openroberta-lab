package lejos.hardware;

public interface Keys {
	
	public static final int ID_UP = 0x1;
	public static final int ID_ENTER = 0x2;
	public static final int ID_DOWN = 0x4;
	public static final int ID_RIGHT = 0x8;
	public static final int ID_LEFT = 0x10;
	public static final int ID_ESCAPE = 0x20;
	public static final int ID_ALL = 0x3f;
	
	public static int NUM_KEYS = 6;
	
	public static final String VOL_SETTING = "lejos.keyclick_volume";
	public static final String LEN_SETTING = "lejos.keyclick_length";
	public static final String FREQ_SETTING = "lejos.keyclick_frequency";

	public void discardEvents();
	
	public int waitForAnyEvent();
	
	public int waitForAnyEvent(int timeout);
	
	public int waitForAnyPress(int timeout);
	
	public int waitForAnyPress();
	
	public int getButtons();
	
	public int readButtons();
	
	public void setKeyClickVolume(int vol);
	
	public int getKeyClickVolume();
	
	public void setKeyClickLength(int len);
	
	public int getKeyClickLength();
	
	public void setKeyClickTone(int key, int freq);
	
	public int getKeyClickTone(int key);
	
}
