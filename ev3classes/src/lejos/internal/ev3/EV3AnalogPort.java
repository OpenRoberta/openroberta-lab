package lejos.internal.ev3;

import java.nio.ByteBuffer;

import lejos.hardware.port.AnalogPort;
import lejos.internal.io.NativeDevice;
import lejos.utility.Delay;

import com.sun.jna.Pointer;

/**
 * This class provides access to the EV3 Analog based sensor ports and other
 * analog data sources.
 * 
 * @author andy
 *
 */
public class EV3AnalogPort extends EV3IOPort implements AnalogPort
{
    protected static final int ANALOG_SIZE = 5172;
    protected static final int ANALOG_PIN1_OFF = 0;
    protected static final int ANALOG_PIN6_OFF = 8;
    protected static final int ANALOG_PIN5_OFF = 16;
    protected static final int ANALOG_BAT_TEMP_OFF = 24;
    protected static final int ANALOG_MOTOR_CUR_OFF = 26;
    protected static final int ANALOG_BAT_CUR_OFF = 28;
    protected static final int ANALOG_BAT_V_OFF = 30;
    protected static final int ANALOG_INDCM_OFF = 5156;
    protected static final int ANALOG_INCONN_OFF = 5160;
    protected static final int ANALOG_NXTCOL_OFF = 4856;
    protected static final int ANALOG_NXTCOL_SZ = 72;
    protected static final int ANALOG_NXTCOL_RAW_OFF = 54;
    protected static NativeDevice dev;
    protected static Pointer pAnalog;
    protected static ByteBuffer inDcm;
    protected static ByteBuffer inConn;
    protected static ByteBuffer shortVals;
    
    // NXT Color sensor stuff
    // data ranges and limits
    protected static final int ADVOLTS = 3300;
    protected static final int ADMAX = 2703;
    protected static final int MINBLANKVAL = (214 / (ADVOLTS / ADMAX));
    protected static final int SENSORMAX = ADMAX;
    protected static final int SENSORRESOLUTION = 1023;
    protected int[][] calData = new int[3][4];
    protected int[] calLimits = new int[2];
    protected short[] rawValues = new short[BLANK_INDEX + 1];
    protected short[] values = new short[BLANK_INDEX + 1];
    protected EV3DeviceManager ldm = EV3DeviceManager.getLocalDeviceManager();

    
    static {
        initDeviceIO();
    }
    /** {@inheritDoc}
     */    
    @Override
    public boolean open(int typ, int port, EV3Port ref)
    {
        int portType = ldm.getPortType(port);
        if (portType == CONN_NXT_IIC || portType == CONN_INPUT_UART)
            return false;
        if (!super.open(typ, port, ref))
            return false;
        //System.out.println("Open port");
        if (portType == CONN_NXT_COLOR)
        {
            //System.out.println("In color mode");
            // Read NXT color sensor calibration data
            getColorData();
        }
        return true;
    }

        
    /**
     * {@inheritDoc}
     */
    @Override
    public float getPin1()
    {
        return (float)shortVals.getShort(ANALOG_PIN1_OFF + port*2)*ADC_REF/ADC_RES;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public float getPin6()
    {
        return (float)shortVals.getShort(ANALOG_PIN6_OFF + port*2)*ADC_REF/ADC_RES;
    }
    
    protected void getColorData()
    {
        setPinMode(TYPE_COLORNONE);
        Delay.msDelay(1000);
        int offset = ANALOG_NXTCOL_OFF + port*ANALOG_NXTCOL_SZ;
        for(int i = 0; i < calData.length; i++)
            for(int j = 0; j < calData[0].length; j++)
            {
                calData[i][j] = shortVals.getInt(offset);
                offset += 4;
                //System.out.println("Cal data[" + i + "][" + j + "] " + calData[i][j]);
            }
        for(int i = 0; i < calLimits.length; i++)
        {
            calLimits[i] = shortVals.getShort(offset);
            offset += 2;
            //System.out.println("Cal limit[" + i + "] " + calLimits[i]);
        }
        
    }
    
    protected void getRawValues()
    {
        int first = ANALOG_NXTCOL_OFF + port*ANALOG_NXTCOL_SZ + ANALOG_NXTCOL_RAW_OFF;
        for(int i = 0; i < rawValues.length; i++)
            rawValues[i] = shortVals.getShort(first + i*2); 
        
    }
    
    /**
     * This method accepts a set of raw values (in full color mode) and processes
     * them using the calibration data to return standard RGB values between 0 and 255
     * @param vals array to return the newly calibrated data.
     */
    private void calibrate(short[] vals)
    {
        // First select the calibration table to use...
        int calTab;
        int blankVal = rawValues[BLANK_INDEX];
        if (blankVal < calLimits[1])
            calTab = 2;
        else if (blankVal < calLimits[0])
            calTab = 1;
        else
            calTab = 0;
        // Now adjust the raw values
        for (int col = RED_INDEX; col <= BLUE_INDEX; col++)
            if (rawValues[col] > blankVal)
                vals[col] = (short) (((rawValues[col] - blankVal) * calData[calTab][col]) >>> 16);
            else
                vals[col] = 0;
        // finally adjust the blank value
        if (blankVal > MINBLANKVAL)
            blankVal -= MINBLANKVAL;
        else
            blankVal = 0;
        blankVal = (blankVal * 100) / (((SENSORMAX - MINBLANKVAL) * 100) / ADMAX);
        if (blankVal > SENSORRESOLUTION)
            blankVal = SENSORRESOLUTION;
        vals[BLANK_INDEX] = (short) ((blankVal * calData[calTab][BLANK_INDEX]) >>> 16);
    }
    
    /**
     * Return a single processed value.
     * If in single color mode this returns a single reading as a percentage. If
     * in full color mode it returns a Lego color value that identifies the
     * color of the object in view.
     * @return processed color value.
     */
    protected short getColor()
    {
        calibrate(values);
        int red = values[RED_INDEX];
        int blue = values[BLUE_INDEX];
        int green = values[GREEN_INDEX];
        int blank = values[BLANK_INDEX];
        // we have calibrated values, now use them to determine the color

        // The following algorithm comes from the 1.29 Lego firmware.
        if (red > blue && red > green)
        {
            // red dominant color
            if (red < 65 || (blank < 40 && red < 110))
                return BLACK;
            if (((blue >> 2) + (blue >> 3) + blue < green) &&
                    ((green << 1) > red))
                return YELLOW;
            if ((green << 1) - (green >> 2) < red)
                return RED;
            if (blue < 70 || green < 70 || (blank < 140 && red < 140))
                return BLACK;
            return WHITE;
        }
        else if (green > blue)
        {
            // green dominant color
            if (green < 40 || (blank < 30 && green < 70))
                return BLACK;
            if ((blue << 1) < red)
                return YELLOW;
            if ((red + (red >> 2)) < green ||
                    (blue + (blue>>2)) < green )
                return GREEN;
            if (red < 70 || blue < 70 || (blank < 140 && green < 140))
                return BLACK;
            return WHITE;
        }
        else
        {
            // blue dominant color
            if (blue < 48 || (blank < 25 && blue < 85))
                return BLACK;
            if ((((red*48) >> 5) < blue && ((green*48) >> 5) < blue) ||
                    ((red*58) >> 5) < blue || ((green*58) >> 5) < blue)
                return BLUE;
            if (red < 60 || green < 60 || (blank < 110 && blue < 120))
                return BLACK;
            if ((red + (red >> 3)) < blue || (green + (green >> 3)) < blue)
                return BLUE;
            return WHITE;
        }
    }
    
    @Override
    public void getFloats(float [] vals, int offset, int length)
    {
        if (length > 0)
        {
            getRawValues();
            int cnt = length;
            if (cnt > rawValues.length)
                cnt = rawValues.length;
            for(int i = 0; i < cnt; i++)
                vals[offset+i] = (float)rawValues[i]*ADC_REF/ADC_RES;
            offset += cnt;
            length -= cnt;
            if (length > 0)
            {
                vals[offset] = getColor();
            }            
        }
    }

    // The following methods provide compatibility with NXT sensors
    
    @Override
    public boolean setType(int type)
    {
        switch(type)
        {
        case TYPE_NO_SENSOR:
        case TYPE_SWITCH:
        case TYPE_TEMPERATURE:
        case TYPE_CUSTOM:
        case TYPE_ANGLE:
            setPinMode(CMD_FLOAT);
            break;
        case TYPE_LIGHT_ACTIVE:
        case TYPE_SOUND_DBA:            
        case TYPE_REFLECTION:
            setPinMode(CMD_SET|CMD_PIN5);
            break;
        case TYPE_LIGHT_INACTIVE:
        case TYPE_SOUND_DB: 
            setPinMode(CMD_SET);
            break;
        case TYPE_LOWSPEED:
            setPinMode(CMD_SET);
            break;
        case TYPE_LOWSPEED_9V:
            setPinMode(CMD_SET|CMD_PIN1);
            break;
        case TYPE_COLORFULL:
        case TYPE_COLORRED:
        case TYPE_COLORGREEN:
        case TYPE_COLORBLUE:
        case TYPE_COLORNONE:
            // Sensor type and pin modes are aligned
            //System.out.println("Set type :" + type);
            setPinMode(type);
            break;

        default:
            return false;
        }
        return true;
    }

    

    // The following methods should arguably be in different class, but they
    // share the same memory structures as those used for analog I/O. Perhaps
    // we need to restructure at some point and mode them to a common private
    // class.
    
    /**
     * Return the analog voltage reading from pin 5 on the output port
     * @param p the port number to return the reading for
     * @return the voltage in mV
     */
    protected static short getOutputPin5(int p)
    {
        return shortVals.getShort(ANALOG_PIN5_OFF + p*2);
    }

    /**
     * Return the battery temperature reading
     * @return
     */
    protected static short getBatteryTemperature()
    {
        return shortVals.getShort(ANALOG_BAT_TEMP_OFF);
    }

    /**
     * return the motor current
     * @return
     */
    protected static short getMotorCurrent()
    {
        return shortVals.getShort(ANALOG_MOTOR_CUR_OFF);
    }

    /**
     * return the battery current
     * @return
     */
    protected static short getBatteryCurrent()
    {
        return shortVals.getShort(ANALOG_BAT_CUR_OFF);
    }

    /**
     * return the battery voltage
     * @return
     */
    protected static short getBatteryVoltage()
    {
        return shortVals.getShort(ANALOG_BAT_V_OFF);
    }

    /**
     * get the type of the port
     * @param port
     * @return
     */
    public static int getPortType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return inConn.get(port);
    }

    /**
     * Get the type of analog sensor (if any) attached to the port
     * @param port
     * @return
     */
    public static int getAnalogSensorType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return inDcm.get(port);
    }
        
    
    private static void initDeviceIO()
    {
        dev = new NativeDevice("/dev/lms_analog"); 
        pAnalog = dev.mmap(ANALOG_SIZE);
        inDcm = pAnalog.getByteBuffer(ANALOG_INDCM_OFF, PORTS);
        inConn = pAnalog.getByteBuffer(ANALOG_INCONN_OFF, PORTS);
        shortVals = pAnalog.getByteBuffer(0, ANALOG_SIZE);
    }
}
