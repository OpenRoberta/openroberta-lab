package lejos.hardware;

public interface Key {
	
	public static final int UP = 0;
	public static final int ENTER = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int LEFT = 4;
	public static final int ESCAPE = 5;
	
	public static int KEY_RELEASED = 0;
	public static int KEY_PRESSED = 1;
	public static int KEY_PRESSED_AND_RELEASED = 2;
	
	public int getId();
	
	public boolean isDown();
	
	public boolean isUp();
	
	public void waitForPress();
	
	public void waitForPressAndRelease();
	
	public void addKeyListener (KeyListener listener);
	
	public void simulateEvent(int event);
	
	public String getName();

};
