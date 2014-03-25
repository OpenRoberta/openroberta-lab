package lejos.internal.ev3;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import lejos.hardware.DeviceException;
import lejos.hardware.port.UARTPort;
import lejos.internal.io.NativeDevice;
import lejos.utility.Delay;

/**
 * Provide access to EV3 sensor ports operating in UART mode.<p><p>
 * NOTE: This code is not pretty! The interface uses a number of structures mapped
 * into memory from the device. I am not aware of any clean way to implement this
 * interface in Java. So for now multiple pointers to bytes/ints array etc. are used this
 * means that the actual offsets of the start of the C arrays needs to be obtained
 * and these (along with various sizes) are currently hard-coded as "OFF" values below.
 * I'm sure there must be a better way! Also note that there seem to be a large
 * number of potential race conditions in the device initialisation stage hence the
 * various loops needed to retry operations.
 * @author andy
 *
 */
public class EV3UARTPort extends EV3IOPort implements UARTPort
{
    protected static NativeDevice uart;
    protected static Pointer pDev;
    protected static ByteBuffer devStatus;
    protected static ByteBuffer raw;
    protected static ByteBuffer actual;
    protected static final int DEV_SIZE = 42744;
    protected static final int DEV_STATUS_OFF = 42608;
    protected static final int DEV_RAW_OFF = 4192;
    protected static final int DEV_RAW_SIZE1 = 9600;
    protected static final int DEV_RAW_SIZE2 = 32;
    protected static final int DEV_ACTUAL_OFF = 42592;
    
    protected static final int UART_SET_CONN = 0xc00c7500;
    protected static final int UART_READ_MODE_INFO = 0xc03c7501;
    protected static final int UART_NACK_MODE_INFO = 0xc03c7502;
    protected static final int UART_CLEAR_CHANGED = 0xc03c7503;
    
    protected static final byte UART_PORT_CHANGED = 1;
    protected static final byte UART_DATA_READY = 8;
    
    protected static final int TIMEOUT_DELTA = 1;
    protected static final int TIMEOUT = 4000;
    protected static final int INIT_DELAY = 5;
    protected static final int INIT_RETRY = 100;
    protected static final int OPEN_RETRY = 5;
    
    static {
        initDeviceIO();
    }
    
    protected EV3DeviceManager ldm = EV3DeviceManager.getLocalDeviceManager();

    /**
     * The following class maps directly to a C structure containing device information.
     * @author andy
     *
     */
    public static class TYPES extends Structure
    {
        public byte Name[] = new byte[12];
        public byte Type;
        public byte Connection;
        public byte Mode;
        public byte DataSets;
        public byte Format;
        public byte Figures;
        public byte Decimals;
        public byte Views;
        public float RawMin;
        public float RawMax;
        public float PctMin;
        public float PctMax;
        public float SiMin;
        public float SiMax;
        public short InvalidTime;
        public short IdValue;
        public byte  Pins;
        public byte[] Symbol = new byte[5];
        public short Align;

        /*
        public TYPES()
        {
            this.setAlignType(Structure.ALIGN_DEFAULT);
        }*/
    }
    
    public static class UARTCTL extends Structure
    {
        public TYPES TypeData;
        public byte Port;
        public byte Mode;
        
        public UARTCTL()
        {
            //this.setAlignType(Structure.ALIGN_DEFAULT);
            //System.out.println("size is " + size());
        }

    }
    
    protected TYPES[] modeInfo = new TYPES[UART_MAX_MODES];
    protected int modeCnt = 0;

    /**
     * return the current status of the port
     * @return status
     */
    protected byte getStatus()
    {
        return devStatus.get(port);
    }

    /**
     * Wait for the port status to become non zero, or for the operation to timeout
     * @param timeout timeout period in ms
     * @return port status or 0 if the operation timed out
     */
    protected byte waitNonZeroStatus(int timeout)
    {
        int cnt = timeout/TIMEOUT_DELTA;
        byte status = getStatus();
        while (cnt-- > 0)
        {
            if (status != 0)
                return status;
            if (ldm.getPortType(port) != CONN_INPUT_UART)
                return status;
            Delay.msDelay(TIMEOUT_DELTA);
            status = getStatus();
        }
        //System.out.println("NZS Timeout");
        return status;       
    }

    /**
     * Wait for the port status to become zero
     * @param timeout timeout period in ms
     * @return zero if successful or the current status if timed out
     */
    protected byte waitZeroStatus(int timeout)
    {
        int cnt = timeout/TIMEOUT_DELTA;
        byte status = getStatus();
        while (cnt-- > 0)
        {
            if (status == 0)
                return status;
            if (ldm.getPortType(port) != CONN_INPUT_UART)
                return status;
            Delay.msDelay(TIMEOUT_DELTA);
            status = getStatus();
        }
        //System.out.println("ZS Timeout");
        return status;       
    }

    /**
     * reset the port and device
     */
    protected void reset()
    {
        // Force the device to reset
        uart.ioctl(UART_SET_CONN, devCon(port, CONN_NONE, 0, 0));        
    }

    /**
     * Set the current operating mode
     * @param mode
     */
    protected void setOperatingMode(int mode)
    {
        uart.ioctl(UART_SET_CONN, devCon(port, CONN_INPUT_UART, 0, mode));        
    }

    /**
     * Read the mode information for the specified operating mode.
     * @param mode mode number to read
     * @param uc control structure to read the data into
     * @return
     */
    protected boolean getModeInfo(int mode, UARTCTL uc)
    {
        uc.Port = (byte)port;
        uc.Mode = (byte)mode;
        uc.write();
        //System.out.println("size is " + uc.size() + " TYPES " + uc.TypeData.size() + " ptr " + uc.getPointer().SIZE);
        uart.ioctl(UART_READ_MODE_INFO, uc.getPointer());
        uc.read();
        //System.out.println("name[0]" + uc.TypeData.Name[0]);
        return uc.TypeData.Name[0] != 0;
    }
    
    /**
     * Clear the flag that indicates the mode info has been cached. This
     * allows us to read the same infomration again later without having to
     * reset the device.
     * @param mode mode number to read
     * @param uc control structure to read the data into
     * @return
     */
    protected void clearModeCache(int mode, UARTCTL uc)
    {
        uc.Port = (byte)port;
        uc.Mode = (byte)mode;
        uc.write();
        //System.out.println("size is " + uc.size() + " TYPES " + uc.TypeData.size() + " ptr " + uc.getPointer().SIZE);
        uart.ioctl(UART_NACK_MODE_INFO, uc.getPointer());
    }

    /**
     * Clear the port changed flag for the current port.
     */
    protected void clearPortChanged()
    {
        //System.out.printf("Clear changed\n");
        uart.ioctl(UART_CLEAR_CHANGED, devCon(port, CONN_INPUT_UART, 0, 0));
        devStatus.put(port, (byte)(devStatus.get(port) & ~UART_PORT_CHANGED));        
    }

    /**
     * Read the mode information from the port. return true 
     * @return
     */
    protected boolean readModeInfo()
    {
        long base = System.currentTimeMillis();
        modeCnt = 0;
        for(int i = 0; i < UART_MAX_MODES; i++)
        {
            UARTCTL uc = new UARTCTL();
            if (getModeInfo(i, uc))
            {
                clearModeCache(i, uc);
                modeInfo[i] = uc.TypeData;
                modeCnt++;
            }
            else
                modeInfo[i] = null;
        }
        //System.out.println("Got " + modeCnt + " entries time " + (System.currentTimeMillis() - base));
        return modeCnt > 0;

    }
    /**
     * Attempt to initialise the sensor ready for use.
     * @param mode initial operating mode
     * @return true if the initialisation succeeded false if it failed
     */
    protected boolean initSensor(int mode)
    {
        byte status;
        int retryCnt = 0;
        //System.out.println("Initial status is " + getStatus());
        long base = System.currentTimeMillis();
        if (ldm.getPortType(port) != CONN_INPUT_UART)
            return false;
        // now try and configure as a UART
        setOperatingMode(mode);
        status = waitNonZeroStatus(TIMEOUT);
        //System.out.println("Time is " + (System.currentTimeMillis() - base));
        while((status & UART_PORT_CHANGED) != 0 && retryCnt++ < INIT_RETRY)
        {
            // something change wait for it to become ready
            if (ldm.getPortType(port) != CONN_INPUT_UART)
                return false;
            clearPortChanged();
            Delay.msDelay(INIT_DELAY);
            status = waitNonZeroStatus(TIMEOUT);
            if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0) 
            {
                // device ready make sure it is now in the correct mode
                setOperatingMode(mode);
                status = waitNonZeroStatus(TIMEOUT);
            }
        }
        //System.out.println("Init complete retry " + retryCnt + " time " + (System.currentTimeMillis() - base));
        if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0)
            return super.setMode(mode);
        else
            return false;
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean initialiseSensor(int mode)
    {
        for(int i = 0; i < OPEN_RETRY; i++)
        {
            if (ldm.getPortType(port) != CONN_INPUT_UART)
                return false;
            // initialise the sensor, if we have no mode data
            // then read it, otherwise use what we have
            if (initSensor(mode) && (modeCnt > 0 || readModeInfo()))
            {
                //System.out.println("reset cnt " + i);
                return true;
            }
            resetSensor();
        }
        return false;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public void resetSensor()
    {
        reset();
        waitZeroStatus(TIMEOUT);
    }


    
    /** {@inheritDoc}
     */    
    @Override
    public boolean open(int typ, int port, EV3Port ref)
    {
        if (ldm.getPortType(port) != CONN_INPUT_UART)
            return false;
        if (!super.open(typ, port, ref))
            return false;
        // clear mode data cache
        modeCnt = 0;
        return true;
    }

    /** {@inheritDoc}
     */    
    @Override
    public void close()
    {
        reset();
        super.close();
    }
    
    /**
     * Set the current operating mode
     * @param mode new mode to set
     * @return true if the mode is set, false if the operation failed
     */
    public boolean setMode(int mode)
    {
        //System.out.println("Set mode " + mode);
        // are we initialised ?
        if (modeCnt <= 0)
            return initialiseSensor(mode);
        if (modeInfo[mode] == null)
            return false;
        //System.out.println("Mode is " + getModeName(mode));
        setOperatingMode(mode);
        int status = waitNonZeroStatus(TIMEOUT);
        //System.out.println("status is " + status);
        boolean ret;
        if ((status & UART_DATA_READY) != 0 && (status & UART_PORT_CHANGED) == 0)
        {
            ret = super.setMode(mode);
        }
        else
        {
            // Sensor may have reset try and initialise it in the new mode.
            ret =  initialiseSensor(mode);
            System.out.println("reset");
        }
        if (ret)
        {
            // wait for new data to be available to ensure we do not return stale values.
            // TODO: Understand why this delay is needed. Some sort of race condition
            // or possibly a delay in the shared memory state being updated.
            Delay.msDelay(20);
            //long s = System.currentTimeMillis();
            ret = waitDataUpdate(TIMEOUT);
            //System.out.println("time " + (System.currentTimeMillis() - s));
        }
        return ret;
    }

    /**
     * The RAW data is held in a circular buffer with 32 bytes of data per entry
     * and 300 entries per port. This method calculates the byte offset of the
     * latest data value read into the buffer.
     * @return offset of the current data
     */
    private int calcRawOffset()
    {
        synchronized (actual)
        {
            return port*DEV_RAW_SIZE1 + actual.getShort(port*2)*DEV_RAW_SIZE2;
        }
    }
    

    /**
     * Wait for a new data point to be added to the data set. Return true if
     * new data is available, false if not
     * @param timeout
     * @return true if updated
     */
    protected boolean waitDataUpdate(int timeout)
    {
        int cnt = timeout/TIMEOUT_DELTA;
        int offset = calcRawOffset();
        while (cnt-- > 0)
        {
            if (calcRawOffset() != offset)
                return true;
            Delay.msDelay(TIMEOUT_DELTA);
        }
        return false;       
    }

    /**
     * Check the sensor status, and if possible recover any from any error.
     * If everything fails throw an exception
     */
    protected void checkSensor()
    {
        if (ldm.getPortType(port) != CONN_INPUT_UART)
            throw new DeviceException("Sensor unavailable");
        if ((getStatus() & UART_PORT_CHANGED) != 0)
        {
            //System.out.println("port " + port + " Changed ");
            // try and reinitialze it
            if (!initialiseSensor(getMode()))
                throw new DeviceException("Sensor changed unable to reset");
                
        }
        
    }
    /**
     * read a single byte from the device
     * @return the byte value
     */
    public byte getByte()
    {
        checkSensor();
        return raw.get(calcRawOffset());
    }

    /**
     * read a number of bytes from the device
     * @param vals byte array to accept the data
     * @param offset offset at which to store the data
     * @param len number of bytes to read
     */
    public void getBytes(byte [] vals, int offset, int len)
    {
        checkSensor();
        int loc = calcRawOffset();
        for(int i = 0; i < len; i++)
            vals[i+offset] = raw.get(loc + i);
    }

    /**
     * read a single short from the device.
     * @return the short value
     */
    public int getShort()
    {
        checkSensor();
        return raw.getShort(calcRawOffset());
    }
    
    /**
     * read a number of shorts from the device
     * @param vals short array to accept the data
     * @param offset offset at which to store the data
     * @param len number of shorts to read
     */
    public void getShorts(short [] vals, int offset, int len)
    {
        checkSensor();
        int loc = calcRawOffset();
        for(int i = 0; i < len; i++)
            vals[i+offset] = raw.getShort(loc + i*2);
    }

    /**
     * Get the string name of the specified mode.<p><p>
     * TODO: Make other mode data available.
     * @param mode mode to lookup
     * @return String version of the mode name
     */
    public String getModeName(int mode)
    {
        if (modeInfo[mode] != null)
            return new String(modeInfo[mode].Name);
        else 
            return "Unknown";
    }

    /**
     * Return the current sensor reading to a string. 
     */
    public String toString()
    {
        float divTable[] = {1f, 10f, 100f, 1000f, 10000f, 100000f};
        TYPES info = modeInfo[currentMode];
        float val;
        switch(info.Format)
        {
        case 0:
            if (info.RawMin >= 0)
                val = getByte() & 0xff;
            else
                val = getByte();
            break;
        case 1:
            if (info.RawMin >= 0)
                val = getShort() & 0xffff;
            else
                val = getShort();
            break;
        // TODO: Sort out other formats
        default:
            val = 0.0f;
        }
        val = val/divTable[info.Decimals];
        String format = "%" + info.Figures + "." + info.Decimals + "f" + new String(info.Symbol);
        return String.format(format, val);        
    }

    /**
     * Reset all of the ports
     */
    public static void resetAll()
    {
        // reset everything
        for(int i = 0; i < PORTS; i++)
            devCon(i, CONN_NONE, 0, 0);
        uart.ioctl(UART_SET_CONN, dc);        
    }
    
    private static void initDeviceIO()
    {
        uart = new NativeDevice("/dev/lms_uart");
        pDev = uart.mmap(DEV_SIZE);
        devStatus = pDev.getByteBuffer(DEV_STATUS_OFF, PORTS);
        actual = pDev.getByteBuffer(DEV_ACTUAL_OFF, PORTS*2);
        raw = pDev.getByteBuffer(DEV_RAW_OFF, PORTS*DEV_RAW_SIZE1);
    }
}
