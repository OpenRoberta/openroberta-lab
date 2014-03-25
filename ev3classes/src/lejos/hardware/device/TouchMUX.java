package lejos.hardware.device;

import lejos.robotics.Touch;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.AnalogSensor;
import lejos.hardware.sensor.SensorConstants;

/**
 * Interface for the Mindsensors Touch Multiplexer.
 * This device allows up to three touch sensors to be attached to a single NXT
 * sensor port.
 *
 * @author Andy
 *
 */
public class TouchMUX extends AnalogSensor {
    /** number of touch sensors supported by this device **/
    public static final int NUMBER_OF_SENSORS = 3;
    /** Bit ID returned by readSensors when sensor T1 is pressed **/
    public static final int ID_T1 = (1 << 0);
    /** Bit ID returned by readSensors when sensor T1 is pressed **/
    public static final int ID_T2 = (1 << 1);
    /** Bit ID returned by readSensors when sensor T1 is pressed **/
    public static final int ID_T3 = (1 << 2);

    private class MuxTouchSensor implements Touch
    {
        private int id;

        /**
         * Create an object to represent one touch sensor attached to the
         * multiplexer.
         * @param touchId The id of this sensor.
         */
        MuxTouchSensor(int touchId)
        {
            id = touchId;
        }

        /**
         * Check if the sensor is pressed.
         * @return <code>true</code> if sensor is pressed, <code>false</code> otherwise.
         */
        public boolean isPressed()
        {
            return (readSensors() & id) != 0;
        }
    }


    /** Instance for the touch sensor connected to port T1 **/
    public final Touch T1 = new MuxTouchSensor(ID_T1);
    /** Instance for the touch sensor connected to port T2 **/
    public final Touch T2 = new MuxTouchSensor(ID_T2);
    /** Instance for the touch sensor connected to port T3 **/
    public final Touch T3 = new MuxTouchSensor(ID_T3);
    
    /**
     * Create a object to provide access to a touch sensor multiplexer
     * @param port The NXT sensor port to which the multiplexer is attached
     */
    public TouchMUX(AnalogPort port)
    {
        super(port);
        //port.setTypeAndMode(SensorConstants.TYPE_CUSTOM, SensorConstants.MODE_RAW);
    }

    /**
     * Create a object to provide access to a touch sensor multiplexer
     * @param port The NXT sensor port to which the multiplexer is attached
     */
    public TouchMUX(Port port)
    {
        super(port);
        this.port.setTypeAndMode(SensorConstants.TYPE_CUSTOM, SensorConstants.MODE_RAW);
    }

    /**
     * Return a Touch interface providing access to the sensor specified by the
     * given id.
     * @param id a number between 0..{@link #NUMBER_OF_SENSORS}-1
     * @return the requested Touch interface
     */
    public synchronized Touch getInstance(int id)
    {
        switch(id)
        {
            case 0:
                return T1;
            case 1:
                return T2;
            case 2:
                return T3;
            default:
                throw new IllegalArgumentException("no such touch sensor port");
        }
    }

    /*
     * Following values provide High and Low limits for the various
     * sensor combinations, as supplied by Mindsensors.
     * Note however that these values assume the RAW readings obtained when the
     * sensor type is set to be a Lego light sensor. With the Lego firmware this
     * is not the actual A/D reading instead it is a value correct for the offset
     * and range of the sensor in the following way...
     * MIN_LS_READING = 200
     * MAX_LS_READING = 900
     * legoVal = MAX_AD_RAW - ((rawVal-MIN_LS_READING)*100)/((MAX_LS_READING - MIN_LS_READING)*100)/MAX_AD_RAW
     * which gives
     * legoVal = MAX_AD_RAW - ((rawVal - MIN_LS_READING)*MAX_AD_RAW)/(MAX_LS_READING - MIN_LS_READING)
     * To correct the values used we must adjust them as follows...
     * lejosVal = (MAX_AD_RAW - legoVal) * (MAX_LS_READING - MIN_LS_READING))/MAX_AD_RAW + MIN_LS_READING
     * The actual values used below have been corrected in this way.....
     * Note also that the Hi/Lo of the test has been changed because the values now drop in value..
     * Original values:
    private static final int L1 = 60;
    private static final int H1 = 160;
    private static final int L2 = 210;
    private static final int H2 = 290;
    private static final int L3 = 500;
    private static final int H3 = 599;
    private static final int L12 = 370;
    private static final int H12 = 460;
    private static final int L13 = 600;
    private static final int H13 = 658;
    private static final int L23 = 659;
    private static final int H23 = 723;
    private static final int L123 = 724;
    private static final int H123 = 800;
     * Adjusted values:
     */
    private static final int H1 = 859;
    private static final int L1 = 791;
    private static final int H2 = 756;
    private static final int L2 = 701;
    private static final int H3 = 558;
    private static final int L3 = 490;
    private static final int H12 = 647;
    private static final int L12 = 585;
    private static final int H13 = 489;
    private static final int L13 = 450;
    private static final int H23 = 449;
    private static final int L23 = 405;
    private static final int H123 = 404;
    private static final int L123 = 352;

    /**
     * Read the touch multiplexer and return a bit mask showing which sensors
     * are currently pressed. Returns ID_T1 if T1 is pressed, ID_T2 if T2 is
     * pressed and ID_T3 id T3 is pressed. If more then one sensor is pressed
     * the return will be an or of the values.
     * @return bit mask showing which sensor buttons are pressed.
     */
    public int readSensors()
    {
        int ret;
        int val = NXTRawIntValue(port.getPin1());
        /*
         * Note the following code can be used to correct the RAW value to be
         * the same as the RAW value for a Lego light sensor. Allowing the use
         * of the original Mindsensor constants.
        if (val > 200)
            val -= 200;
        else
            val = 0;
        val = (val*100)/68;
        val = 1023 - val
         */
        if (L1 <= val && val < H1)
            ret = ID_T1;
        else if (L2 <= val && val < H2)
            ret = ID_T2;
        else if (L3 <= val && val < H3)
            ret = ID_T3;
        else if (L12 <= val && val < H12)
            ret = ID_T1|ID_T2;
        else if (L13 <= val && val < H13)
            ret = ID_T1|ID_T3;
        else if (L23 <= val && val < H23)
            ret = ID_T2|ID_T3;
        else if (L123 <= val && val < H123)
            ret = ID_T1|ID_T2|ID_T3;
        else
            ret = 0;
        return ret;
    }

}
