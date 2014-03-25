package lejos.hardware;

/**
 * Abstraction for an NXT button. Example:
 * 
 * <pre>
 * Button.ENTER.waitForPressAndRelease();
 * Sound.playTone(1000, 1);
 * </pre>
 * 
 * <b>Notions:</b> The API is designed around two notions: states (up / down)
 * and events (press / release). It is said that a button is pressed (press
 * event), if its state changes from up to down. Similarly, it is said that a
 * button is released (release event), if its states changed from down to up.
 * 
 * <b>Thread Safety</b>: All methods that return buttons states can be used
 * safely from multiple threads, even while a call to one of the waitFor*
 * methods active. However, it is not safe to invoke waitFor* methods in
 * parallel from different threads. This includes the waitFor* methods of
 * different buttons. For example Button.ENTER.waitForPress() must not be
 * invoked in parallel to Button.ESCAPE.waitForPress() or the static
 * Button.waitForAnyEvent(). In case this is needed, it is strongly recommended
 * that you write your own Thread, which waits for button events and dispatches
 * the events to anyone who's interested.
 */
public class Button {

	public static final int ID_UP = 0x1;
	public static final int ID_ENTER = 0x2;
	public static final int ID_DOWN = 0x4;
	public static final int ID_RIGHT = 0x8;
	public static final int ID_LEFT = 0x10;
	public static final int ID_ESCAPE = 0x20;
	public static final int ID_ALL = 0x3f;

	public static final String VOL_SETTING = "lejos.keyclick_volume";
	public static final String LEN_SETTING = "lejos.keyclick_length";
	public static final String FREQ_SETTING = "lejos.keyclick_frequency";

	/**
	 * The Enter button.
	 */
	public static final Key ENTER = BrickFinder.getDefault().getKey("Enter");
	/**
	 * The Left button.
	 */
	public static final Key LEFT = BrickFinder.getDefault().getKey("Left");
	/**
	 * The Right button.
	 */
	public static final Key RIGHT = BrickFinder.getDefault().getKey("Right");
	/**
	 * The Escape button.
	 */
	public static final Key ESCAPE = BrickFinder.getDefault().getKey("Escape");
	/**
	 * The Up button.
	 */
	public static final Key UP = BrickFinder.getDefault().getKey("Up");
	/**
	 * The Down button.
	 */
	public static final Key DOWN = BrickFinder.getDefault().getKey("Down");

	public static final Keys keys = BrickFinder.getDefault().getKeys();

	private Button(int aCode) {
		// Don't use
	}

	/**
	 * This method discards any events. In contrast to {@link #readButtons()},
	 * this method doesn't beep if a button is pressed.
	 */
	public static void discardEvents() {
		keys.discardEvents();
	}

	/**
	 * Waits for some button to be pressed or released. Which buttons have been
	 * released or pressed is returned as a bitmask. The lower eight bits (bits
	 * 0 to 7) indicate, which buttons have been pressed. Bits 8 to 15 indicate
	 * which buttons have been released.
	 * 
	 * @return the bitmask
	 * @see #ID_ENTER
	 * @see #ID_LEFT
	 * @see #ID_RIGHT
	 * @see #ID_ESCAPE
	 */
	public static int waitForAnyEvent() {
		return keys.waitForAnyEvent();
	}

	/**
	 * Waits for some button to be pressed or released. Which buttons have been
	 * released or pressed is returned as a bitmask. The lower eight bits (bits
	 * 0 to 7) indicate, which buttons have been pressed. Bits 8 to 15 indicate
	 * which buttons have been released.
	 * 
	 * @param timeout
	 *            The maximum number of milliseconds to wait
	 * @return the bitmask
	 * @see #ID_ENTER
	 * @see #ID_LEFT
	 * @see #ID_RIGHT
	 * @see #ID_ESCAPE
	 */
	public static int waitForAnyEvent(int timeout) {
		return keys.waitForAnyEvent(timeout);
	}

	/**
	 * Waits for some button to be pressed. If a button is already pressed, it
	 * must be released and pressed again.
	 * 
	 * @param timeout
	 *            The maximum number of milliseconds to wait
	 * @return the ID of the button that has been pressed or in rare cases a
	 *         bitmask of button IDs, 0 if the given timeout is reached
	 */
	public static int waitForAnyPress(int timeout) {
		return keys.waitForAnyPress(timeout);
	}

	/**
	 * Waits for some button to be pressed. If a button is already pressed, it
	 * must be released and pressed again.
	 * 
	 * @return the ID of the button that has been pressed or in rare cases a
	 *         bitmask of button IDs
	 */
	public static int waitForAnyPress() {
		return waitForAnyPress(0);
	}

	/**
	 * <i>Low-level API</i> that reads status of buttons.
	 * 
	 * @return An integer with possibly some bits set: {@link #ID_ENTER} (ENTER
	 *         button pressed) {@link #ID_LEFT} (LEFT button pressed),
	 *         {@link #ID_RIGHT} (RIGHT button pressed), {@link #ID_ESCAPE}
	 *         (ESCAPE button pressed). If all buttons are released, this method
	 *         returns 0.
	 */
	public static int getButtons() {
		return keys.getButtons();
	}

	/**
	 * <i>Low-level API</i> that reads status of buttons.
	 * 
	 * @return An integer with possibly some bits set: {@link #ID_ENTER} (ENTER
	 *         button pressed) {@link #ID_LEFT} (LEFT button pressed),
	 *         {@link #ID_RIGHT} (RIGHT button pressed), {@link #ID_ESCAPE}
	 *         (ESCAPE button pressed). If all buttons are released, this method
	 *         returns 0.
	 */
	public static int readButtons() {
		return keys.readButtons();
	}

	/**
	 * Set the volume used for key clicks
	 * 
	 * @param vol
	 */
	public static void setKeyClickVolume(int vol) {
		keys.setKeyClickVolume(vol);
	}

	/**
	 * Return the current key click volume.
	 * 
	 * @return current click volume
	 */
	public static synchronized int getKeyClickVolume() {
		return keys.getKeyClickVolume();
	}

	/**
	 * Set the len used for key clicks
	 * 
	 * @param len
	 *            the click duration
	 */
	public static synchronized void setKeyClickLength(int len) {
		keys.setKeyClickLength(len);
	}

	/**
	 * Return the current key click length.
	 * 
	 * @return key click duration
	 */
	public static synchronized int getKeyClickLength() {
		return keys.getKeyClickLength();
	}

	/**
	 * Set the frequency used for a particular key. Setting this to 0 disables
	 * the click. Note that key may also be a corded set of keys.
	 * 
	 * @param key
	 *            the NXT key
	 * @param freq
	 *            the frequency
	 */
	public static synchronized void setKeyClickTone(int key, int freq) {
		keys.setKeyClickTone(key, freq);
	}

	/**
	 * Return the click freq for a particular key.
	 * 
	 * @param key
	 *            The key to obtain the tone for
	 * @return key click duration
	 */
	public static synchronized int getKeyClickTone(int key) {
		return keys.getKeyClickTone(key);
	}

	public static void LEDPattern(int pattern) {
		BrickFinder.getDefault().getLED().setPattern(pattern);
	}

}
