package lejos.remote.nxt;

import lejos.internal.io.SystemSettings;

/**
 * Base class for nxt communications devices. Provides a common address/name,
 * plus utility functions. 
 * @author andy
 */
abstract public class NXTCommDevice
{
    public static final int ADDRESS_LEN = 6;
    public static final int NAME_LEN = 16;
    public static final String SERIAL_NO = "lejos.usb_serno";
    public static final String NAME = "lejos.usb_name";

    static String devAddress = "123";
    static String devName = "xxx";

    static {
        loadSettings();
    }

    /**
     * Determine if a string contains a Bluetooth style address.
     * @param s String to test
     * @return true if the string is an address
     */
    public static boolean isAddress(String s)
    {
        if (s == null || s.length() < 2) return false;
        return s.charAt(0) == '0' && s.charAt(1) == '0';
    }


    /**
     * Convert a string version of a Bluetooth address into a byte array
     * address.
     * @param strAddress The string version of the address
     * @return a byte array version of the address
     */
    public static byte[] stringToAddress(String strAddress)
    {
        if (strAddress != null && strAddress.length() == (ADDRESS_LEN)*2)
        {
            // Convert to binary format
            byte[] addr = new byte[ADDRESS_LEN];
            int out = 0;
            for(int i = 0; i < strAddress.length(); i += 2)
            {
                char c = strAddress.charAt(i);
                byte val = (byte)((c > '9' ? c - 'A' + 10 : c - '0') << 4);
                c = strAddress.charAt(i+1);
                val |= (byte)(c > '9' ? c - 'A' + 10 : c - '0');
                addr[out++] = val;
            }
            return addr;
        }
        else
            return null;
    }

    /**
     * Convert the string version of a devName into a byte array.
     * @param strName string version of the devName
     * @return byte array containing the devName.
     */
    public static byte[] stringToName(String strName)
    {
        if (strName != null && strName.length() <= NAME_LEN)
        {
            byte[] nam = new byte[NAME_LEN];
            for(int i = 0; i < strName.length(); i++)
            {
                nam[i] = (byte)strName.charAt(i);
            }
            return nam;
        }
        else
            return null;
    }

	protected static final char[] cs = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

	/**
	 * Helper method to convert address byte array to String.
	 * @param addr A byte array of bytes containing the address.
	 * @return String representation of Bluetooth address.
	 */
	public static String addressToString(byte [] addr)
    {
        if (addr == null || addr.length < ADDRESS_LEN) return null;
		char[] caddr = new char[ADDRESS_LEN*2];

		int ci = 0;
		int addri = 0;

		for(int i=0; i<ADDRESS_LEN; i++) {
			addri = (int)addr[i] & 0xff;
			caddr[ci++] = cs[addri / 16];
			caddr[ci++] = cs[addri % 16];
		}
		return new String(caddr);
	}

	/**
	 * Return a string version of a device devName held as a byte array
	 * @param name
	 * @return string version of devName
	 */
	public static String nameToString(byte[] name)
	{
		if (name == null || name.length == 0)
			return null;

		int len = name.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len && name[i] != 0; i++)
			sb.append((char)(name[i] & 0xFF));
		return sb.toString();
	}

   /**
     * Set the USB serial number. Should be a unique 12 character String
     * @param sn
     */
    public static void setAddress(String sn)
    {
        devAddress = sn;
    }

    /**
     * Return the current USB serial number.
     * @return the serial number
     */
    public static String getAddress()
    {
        return devAddress;
    }

    /**
     * Set the USB devName. Can be up to 16 character String
     * @param nam the mame
     */
    public static void setName(String nam)
    {
        devName = nam;
    }

    /**
     * Return the current USB devName.
     * @return the devName
     */
    public static String getName()
    {
        return devName;
    }

    /**
     * Load the current system settings associated with this class. Called
     * automatically to initialize the class. May be called if it is required
     * to reload any settings.
     */
    public static void loadSettings()
    {
        devAddress = SystemSettings.getStringSetting(SERIAL_NO, "123456780090");
        devName = SystemSettings.getStringSetting(NAME, "nxt");
    }

}
