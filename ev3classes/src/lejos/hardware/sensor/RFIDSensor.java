package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

/**
 * Support for the <a href="http://www.codatex.com">Codatex RFID Sensor</a>.
 * This device requires delays between various commands for them to
 * function correctly, it also enters a sleep mode and requires to be woken up.
 * The methods in this class fall into two categories.
 * Basic commands
 * These pretty much match one to one with the device command set. They do not
 * incorporate any delays, or wake up code. They can be used by user programs
 * but if they are then appropriate delays etc. must be used. They are provided
 * to allow more sophisticated user programs access to the low level device.
 *
 * High level commands
 * These provide higher level access to the device and are often implemented via
 * several i2c commands. They do include delays and wake up logic.
 *
 * @author andy
 */
public class RFIDSensor extends I2CSensor
{
    /*
     * Notes
     * This sensor is a little tricky to deal with. It will enter a sleep mode
     * if not used for a while and so will need to be woken up. Plus it seems
     * very sensitive to timing of requests (hence the use of various delays).
     */
    // Command Registers and bytes
    private static final int REG_CMD = 0x41;
    private static final byte CMD_STOP = 0;
    private static final byte CMD_SINGLEREAD = 1;
    private static final byte CMD_CONTINUOUSREAD = 2;
    private static final byte CMD_BOOTLOADER = (byte)0x81;
    private static final byte CMD_STARTFIRMWARE = (byte)0x83;

    // Data registers
    private static final int REG_STATUS = 0x32;
    private static final int REG_DATA = 0x42;
    private static final int LEN_DATA = 5;
    private static final int REG_SERIALNO = 0xA0;
    private static final int LEN_SERIALNO = 16;

    private static final int DEFAULT_ADDRESS = 4;

    // Various delays
    private static final int DELAY_WAKEUP = 5;
    private static final int DELAY_FIRMWARE = 100;
    private static final int DELAY_ACQUIRE = 250;
    private static final int DELAY_READ = 200;

    private byte buf1[] = new byte[1];
    private long nextRead = now();

    private void init()
    {
        // We seem to need to give the device a bit of a kick to get it going
        // doing a read seems to do the job...
        wakeUp();
        readTransponder(false);
    }
    /**
     * Create a class to provide access to the device. Perform device
     * initialization.
     * @param port The sensor port to use for this device.
     */
    public RFIDSensor(I2CPort port)
    {
        super(port, DEFAULT_ADDRESS);
        init();
    }
    
    /**
     * Create a class to provide access to the device. Perform device
     * initialization.
     * @param port The sensor port to use for this device.
     */
    public RFIDSensor(Port port)
    {
        super(port, DEFAULT_ADDRESS, TYPE_LOWSPEED);
        init();
    }

    /**
     * Helper function, return current time, used as basis of required inter
     * command delays.
     * @return
     */
    private long now()
    {
        return System.currentTimeMillis();
    }

    /**
     * Helper function wait until a specific time has arrived
     */
    private void waitUntil(long target)
    {
        long d = target - now();
        if (d > 0)
            Delay.msDelay(d);
    }


    /**
     * The sensor will go into a power save mode after a short time. This means
     * that we will need to wake it up before issuing commands. Includes
     * the required Delay.msDelay.
     */
    public void wakeUp()
    {
        // Simply send a dummy command to the device
        sendData(0, (byte)0);
        Delay.msDelay(DELAY_WAKEUP);
    }


    /**
     * We over-ride the default implementation to ensure that the device is
     * awake before we talk to it.
     * @param register The register to read the string from.
     * @param len
     * @return The requested string.
     */
    protected String fetchString(byte register, int len)
    {
        wakeUp();
        return super.fetchString(register, len);
    }

    /**
     * Start the firmware on the RFID device.
     * NOTES: It seems that you need to issue this command (or some other
     * firmware command), prior to attempting to read the version number etc.
     * Does not wake up the device or contain any delays.
     */
    public void startFirmware()
    {
        sendData(REG_CMD, CMD_STARTFIRMWARE);
    }

    /**
     * Enter boot loader mode.
     * Does not wake up the device or include any delays.
     *
     */
    public void startBootLoader()
    {
        sendData(REG_CMD, CMD_BOOTLOADER);
    }

    /**
     * Obtain the serial number of the RFID Sensor.
     * NOTES: To obtain the serial number the device must be in boot loader
     * mode. This function will switch into this mode and then return to
     * normal mode on completion.
     * @return the 12 byte serial number if ok or null if there is an error
     */
    public byte [] getSerialNo()
    {
        wakeUp();
        // must be in boot loader mode
        startBootLoader();
        Delay.msDelay(DELAY_FIRMWARE);
        byte [] ret = new byte[LEN_SERIALNO];
        getData(REG_SERIALNO, ret, ret.length);
        startFirmware();
        return ret;
    }

    /**
     * Read the status from the device.
     * Does not wake up the device or include any delays.
     * @return 1 data available 0 no data < 0 error
     */
    public int getStatus()
    {
        getData(REG_STATUS, buf1, buf1.length);
        return (int)buf1[0];
    }

    /**
     * Start a single read from the device.
     * Does not wake up the device or include any delays.
     */
    public void startSingleRead()
    {
        sendData(REG_CMD, CMD_SINGLEREAD);
    }

    /**
     * Start continually reading from the device.
     * Does not wake up the device or include any delays.
     */
    public void startContinuousRead()
    {
        sendData(REG_CMD, CMD_CONTINUOUSREAD);
    }

    /**
     * Send a stop command to the device.
     * Places the device into sleep mode.
     * @return < 0 i2c error code >= 0 no error
     */
    public void stop()
    {
        sendData(REG_CMD, CMD_STOP);
    }

    /**
     * Read a transponder id.
     * Reads the transponder using either continuous or single shot mode. If
     * using single shot mode the tag must me available now. If using continuous
     * mode then after the first call, the tag can be presented at any time
     * and a subsequent read will return the results.
     * @param continuous Should we use continuous mode
     * @return null if error or no data available, otherwise an array of five id bytes
     */
    public byte [] readTransponder(boolean continuous)
    {
        // Wait until a new read is valid
        waitUntil(nextRead);
        wakeUp();
        if (continuous)
        {
            // If not ready, start a new command
            if (getStatus() <= 0)
            {
                startContinuousRead();
                Delay.msDelay(DELAY_ACQUIRE);
            }
        }
        else
        {
            startSingleRead();
            Delay.msDelay(DELAY_ACQUIRE);
        }
        // make a note of when it is safe to do another read.
        nextRead = now() + DELAY_READ;
        // Get the data
        boolean valid = false;
        byte [] ret = new byte[LEN_DATA];
        for(int i = 0; i < ret.length; i++)
        {
            getData(REG_DATA+i, buf1, buf1.length);
            ret[i] = buf1[0];
            if (buf1[0] != 0)
                valid = true;
        }
        return (valid ? ret : null);
    }

    public long readTransponderAsLong(boolean continuous)
    {
        byte [] id = readTransponder(continuous);
        if (id == null) return 0;
        long ret = 0;
        for(int i = id.length - 1; i >= 0; i--)
        {
            ret <<= 8;
            ret |= ((long)id[i] & 0xff);
        }
        return ret;
    }
}

