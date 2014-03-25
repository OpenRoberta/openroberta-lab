package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;
import lejos.utility.Delay;

/**
 * LeJOS driver for the Mindsensors NumericPad.
 * 
 * @author Rudolf Lapie
 */
public class MindSensorsNumPad extends I2CSensor {
	// Driver was originally contributed by Rudolf Lapie
	// Interface similar to lejos.nxt.Button added by Sven Köhler
	
	// instance variables
	private static final int NP_ADDRESS = 0xB4; // Default Address
	private static final int NP_DATA_REG = 0x00; // Data Register
	private static final int POLL_INTERVAL = 10;
	
	// Indexes of 0123456789*# in #9630825*714
	private static final String IDXMAP = "\04\12\06\03\13\07\02\11\05\01\10\00";
	// private static final String KEYMAP = "0123456789*#";
	
	private final byte[] ioBuf = new byte[2];
	private int curButtons;

	// some day, we will throw exception and get ride of this ERROR constant.
	public static final int ERROR = -1;
	public static final int ID_KEY0 = (1 << 0);
	public static final int ID_KEY1 = (1 << 1);
	public static final int ID_KEY2 = (1 << 2);
	public static final int ID_KEY3 = (1 << 3);
	public static final int ID_KEY4 = (1 << 4);
	public static final int ID_KEY5 = (1 << 5);
	public static final int ID_KEY6 = (1 << 6);
	public static final int ID_KEY7 = (1 << 7);
	public static final int ID_KEY8 = (1 << 8);
	public static final int ID_KEY9 = (1 << 9);
	public static final int ID_STAR = (1 << 10);
	public static final int ID_HASH = (1 << 11);


	protected void init()
	{

        // This is the initialization used by the Xander's RobotC driver
        // and the code submitted by Rudolf Lapie.
        sendGroup(0x2B, "\001\001\000\000\001\001\377\002");
        sendGroup(0x41, "\017\012\017\012\017\012\017\012\017\012\017\012\017\012");
        sendGroup(0x4F, "\017\012\017\012\017\012\017\012\017\012\017\012");
        sendGroup(0x5C, "\013\040\014");
        sendGroup(0x7B, "\013");
        sendGroup(0x7D, "\234\145\214");
        
        // This are the initialization as in the NXC driver by MindSensors.
        // For some reason, it differs from the one the RobotC driver and
        // the driver submitted by Rudolf Lapie.
        // sendGroup(0x2B, "\001\001\000\000\001\001\377\002");
        // sendGroup(0x41, "\017\012\017\012\017\012\017\012\017");
        // sendGroup(0x4A, "\012\017\012\017\012\017\012\017");
        // sendGroup(0x52, "\012\017\012\017\012\017\012\017");
        // sendGroup(0x5C, "\013\040\014");
        // sendGroup(0x7D, "\234\145\214");
        
        int k = this.readButtonsRaw();
        if (k == ERROR)
            k = 0;
        this.curButtons = k;

	}

    /**
     * Constructor for objects of the NumericPad of Mindsensors. It assumes that
     * you didn't change the I²C address of the device.
     * 
     * @param port
     *            the port number (SensorPort.S1 ... SensorPort.S4) to which the
     *            numeric pad is connected.
     */
    public MindSensorsNumPad(I2CPort port) {
        super(port, NP_ADDRESS);
        init();
    }
    
    /**
     * Constructor for objects of the NumericPad of Mindsensors. It assumes that
     * you didn't change the I²C address of the device.
     * 
     * @param port
     *            the port number (SensorPort.S1 ... SensorPort.S4) to which the
     *            numeric pad is connected.
     */
    public MindSensorsNumPad(Port port) {
        super(port, NP_ADDRESS);
        init();
    }

	private void sendGroup(int reg, String data) {
		int len = data.length();
		byte[] b = new byte[len];
		for (int i=0; i<len; i++)
			b[i] = (byte)data.charAt(i);
		this.sendData(reg, b, 0, len);
	}
	
	/**
	 * Returns a Bitmask. Bits 0 to 9 correspond to the Number Keys 0 to 9.
	 * Bit 10 corresponds to the star and Bit 11 to the hash key.
	 * @return the bitmask, or {@link #ERROR} if an I2C error has occured
	 */
	public int readButtons() {
		int k = readButtonsRaw();
		if (k == ERROR)
			return k;
		
		return rawToOrdered(k);
	}

	private int readButtonsRaw() {
		getData(NP_DATA_REG, ioBuf, 2);
		
		return ((ioBuf[1] & 0xFF) << 8) + (ioBuf[0] & 0xFF);
	}
	
	/**
	 * This method discards any events.
	 */
	public void discardEvents() {
		this.curButtons = this.readButtonsRaw();
	}

	/**
	 * Waits for some button to be pressed or released.
	 * Which buttons have been released or pressed is returned as a bitmask.
	 * The lower twenve bits (bits 0 to 11) indicate, which buttons have been pressed.
	 * Bits 16 to 27 indicate which buttons have been released.
	 * 
	 * @return the bitmask 
	 */
	public int waitForAnyEvent() {
		return this.waitForAnyEvent(0);
	}
	
	/**
	 * Waits for some button to be pressed or released.
	 * Which buttons have been released or pressed is returned as a bitmask.
	 * The lower twenve bits (bits 0 to 11) indicate, which buttons have been pressed.
	 * Bits 16 to 27 indicate which buttons have been released.
	 * 
	 * @param timeout The maximum number of milliseconds to wait
	 * @return the bitmask 
	 */
	public int waitForAnyEvent(int timeout) {
		long end = (timeout == 0 ? 0x7fffffffffffffffL : System.currentTimeMillis() + timeout);
		int oldDown = curButtons;
		while (true)
		{
			int newDown = readButtonsRaw();
			if (newDown == ERROR)
				newDown = oldDown;
			
			curButtons = newDown;
			if (oldDown != newDown)
			{
				int pressed = newDown & (~oldDown);
				int released =  oldDown & (~newDown);
				return (rawToOrdered(released) << 16) | rawToOrdered(pressed);
			}
			
			long toWait = end - System.currentTimeMillis();
			if (toWait <= 0)
				return 0;
			if (toWait > POLL_INTERVAL)
				toWait = POLL_INTERVAL;
			Delay.msDelay(toWait);
			
			oldDown = newDown;
		}
	}

	/**
	 * Waits for some button to be pressed. If a button is already pressed, it
	 * must be released and pressed again.
	 * 
	 * @return the ID of the button that has been pressed or in rare cases a bitmask of button IDs
	 */
	public int waitForAnyPress() {
		return this.waitForAnyPress(0);
	}
	
	/**
	 * Waits for some button to be pressed. If a button is already pressed, it
	 * must be released and pressed again.
	 * 
	 * @param timeout The maximum number of milliseconds to wait
	 * @return the ID of the button that has been pressed or in rare cases a bitmask of button IDs,
	 *         0 if the given timeout is reached 
	 */
	public int waitForAnyPress(int timeout) {
		long end = (timeout == 0 ? 0x7fffffffffffffffL : System.currentTimeMillis() + timeout);

		int oldDown = curButtons;
		while (true)
		{
			int newDown = readButtonsRaw();
			if (newDown == ERROR)
				newDown = oldDown;
			
			curButtons = newDown;
			int pressed = newDown & (~oldDown);
			if (pressed != 0)
				return rawToOrdered(pressed);
			
			long toWait = end - System.currentTimeMillis();
			if (toWait <= 0)
				return 0;
			if (toWait > POLL_INTERVAL)
				toWait = POLL_INTERVAL;
			Delay.msDelay(toWait);
			
			oldDown = newDown;
		}
	}

	private int rawToOrdered(int x)
	{
		int r = 0;
		int len = IDXMAP.length();
		for (int i=0; i<len; i++)
		{
			int j = IDXMAP.charAt(i);
			r |= ((x >> j) & 1) << i;
		}
		return r;
	}
	
//	private String rawToString(int x)
//	{
//		StringBuilder sb = new StringBuilder(12);
//		int len = IDXMAP.length();
//		for (int i=0; i<len; i++)
//		{
//			int j = IDXMAP.charAt(i);
//			if ((x & (1 << j)) != 0)
//				sb.append(KEYMAP.charAt(i));
//		}
//		return sb.toString();
//	}

	/**
	 * The lower 12 bits (Bits 0 to 11) are converted to a string
	 * containing 0 to 9, *, and # depending on whether which of bits are set.
	 * The returned string contains these character in that order.
	 */
	public static String maskToString(int x)
	{
		StringBuilder sb = new StringBuilder(12);
		for (int i=0; i<10; i++)
			if ((x & (1 << i)) != 0)
				sb.append((char)('0'+i));
		if ((x & ID_STAR) != 0)
			sb.append('*');
		if ((x & ID_HASH) != 0)
			sb.append('#');
		return sb.toString();
	}
}