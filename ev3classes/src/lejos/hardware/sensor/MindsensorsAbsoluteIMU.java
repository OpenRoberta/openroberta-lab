package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.Calibrate;

/**
 * This class implements the standard leJOS sensor interface for the Mindsensors
 * AbsoluteIMU family of sensors.<br>
 * See: <a href="http://www.mindsensors.com/">Mindsensors.com</a><br>
 * The information used for this device is based on<b>
 * <a href="http://www.mindsensors.com/index.php?module=documents&JAS_DocumentManager_op=downloadFile&JAS_File_id=1303">Mindsensors IMU user guide</a><br>
 * @author andy
 *
 */
public class MindsensorsAbsoluteIMU extends I2CSensor implements SensorModes, Calibrate
{
    /** The default I2C address of the sensor */
    public static final int DEFAULT_I2C_ADDRESS = 0x22;
    protected static final int ACCEL_DATA = 0x45;
    protected static final int COMPASS_DATA = 0x4b;
    protected static final int MAG_DATA = 0x4d;
    protected static final int GYRO_DATA = 0x53;
    protected static final int COMMAND = 0x41;
    protected static final int GYRO_FILTER = 0x5a;
    protected static final byte SENSITIVITY_BASE = 0x31;
    protected static final byte START_CALIBRATION = 0x43;
    protected static final byte END_CALIBRATION = 0x63;
    public static final int LOW = 0;
    public static final int MEDIUM = 1;
    public static final int HIGH = 2;
    public static final int VERY_HIGH = 3;
    static final float[] gyroScale = {1.0f, 2.0f, 8.0f, 8.0f};
    static final float[] magneticToSI = {1.0f/1100f, 1.0f/1100f, 1.0f/980f};
    static final float[] accelerationToSI = {-0.00981f, 0.00981f, 0.00981f};
    protected ShortSensorMode accelMode;
    protected ShortSensorMode magMode;
    protected ShortSensorMode gyroMode;
    protected ShortSensorMode compassMode;
    
    protected class ShortSensorMode implements SensorMode
    {
        protected final String name;
        protected final int sampleSize;
        protected final float[] convert;
        protected final int baseReg;
        protected float[] scale;
        protected byte[] buffer;

        /**
         * Internal class to provide the sensor data. Handles obtaining the base
         * data and converting it to suitable SI units.
         * @param name mode name
         * @param reg base register for the available samples
         * @param sampleSize number of samples
         * @param convert conversion factor to SI units
         * @param scale scale factor needed for range adjustment
         */
        protected ShortSensorMode(String name, int reg, int sampleSize, float[] convert, float scale)
        {
            this.name = name;
            this.sampleSize = sampleSize;
            this.convert = convert;
            this.baseReg = reg;
            this.scale = new float[sampleSize];
            setScale(scale);
            buffer = new byte[sampleSize*2];
        }
        
        protected ShortSensorMode(String name, int reg, int sampleSize, float convert, float scale)
        {
            this.convert= new float[sampleSize];
            for(int i = 0; i < sampleSize; i++)
                this.convert[i] = convert;
            this.name = name;
            this.sampleSize = sampleSize;
            this.baseReg = reg;
            this.scale = new float[sampleSize];
            setScale(scale);
            buffer = new byte[sampleSize*2];
        }

        /**
         * Set the scale factor used when converting data to returned units.
         * @param scale new scale factor
         */
        protected void setScale(float scale)
        {
            for(int i = 0; i < convert.length; i++)
                this.scale[i] = convert[i]*scale;
        }

        @Override
        public int sampleSize()
        {
            return sampleSize;
        }

        @Override
        public void fetchSample(float[] sample, int offset)
        {
            // fetch the raw data
            getData(baseReg, buffer, 0, buffer.length);
            for(int i = 0; i < sampleSize; i++)
            {
                int rawVal = (buffer[i*2] & 0xff) | ((buffer[i*2+1]) << 8);
                sample[i+offset] = rawVal*scale[i];
            }
        }

        @Override
        public String getName()
        {
            return name;
        }
        
    }
    
    protected void init()
    {
        // The accelerometer reports readings in mG we convert this to m/s/s
        // TODO: we switch X axis direction to match dexter device. Do we need to do anything with the gyro etc?
        accelMode = new ShortSensorMode("Acceleration", ACCEL_DATA, 3, accelerationToSI, 1.0f);
        // the magnetometer reports in Gauss
        magMode = new ShortSensorMode("Magnetic", MAG_DATA, 3, magneticToSI, 1.0f);
        // the gyro reports in units of 8.75 milli-degree/s we convert to degree/s
        gyroMode = new ShortSensorMode("Rate", GYRO_DATA, 3, 0.00875f, 1.0f);
        // the compass reports the angle in degrees.
        compassMode = new ShortSensorMode("Compass", COMPASS_DATA, 1, 1.0f, 1.0f);
        this.setModes(new SensorMode[] {compassMode, accelMode, magMode, gyroMode});
        setRange(LOW);
        setGyroFilter(4);
    }

    public MindsensorsAbsoluteIMU(I2CPort port, int address)
    {
        super(port, address);
        init();

    }

    public MindsensorsAbsoluteIMU(I2CPort port)
    {
        this(port, DEFAULT_I2C_ADDRESS);
    }

    public MindsensorsAbsoluteIMU(Port port, int address)
    {
        super(port, address);
        init();
    }

    public MindsensorsAbsoluteIMU(Port port)
    {
        this(port, DEFAULT_I2C_ADDRESS);
    }
    
    /**
     * Return a SensorMode object that will acceleration data for the X, Y and Z axis.
     * The data is returned in units of degress.
     * @return a SensorMode object
     */
    public SensorMode getCompassMode()
    {
        return getMode(0);
    }

    /**
     * Return a SensorMode object that will acceleration data for the X, Y and Z axis.
     * The data is returned in units of m/s/s.
     * @return a SensorMode object
     */
    public SensorMode getAccelerationMode()
    {
        return getMode(1);
    }

    /**
     * Return a SensorMode object that will return Magnetic data for the X, Y and Z axis
     * The data is returned in Guass 
     * @return a SensorMode object
     */
    public SensorMode getMagneticMode()
    {
        return getMode(2);
    }
    
    /**
     * Return a SensorMode object that will angular velocity data for the X, Y and Z axis.
     * The data is returned in units of degrees/s.
     * @return a SensorMode object
     */
    public SensorMode getRateMode()
    {
        return getMode(3);
    }

    /**
     * Set the sensitivity used by the sensor. This setting impacts the maximum
     * range of the returned value and the resolution of the reading.<br>
     * LOW Acceleration 2G Gyro 250 degrees/second<br>
     * MEDIUM Acceleration 4G Gyro 500 degrees/second<br>
     * HIGH Acceleration 8G Gyro 2000 degrees/second<br>
     * VERY_HIGH Acceleration 16G Gyro 2000 degrees/second<br>
     * The default setting is LOW.
     * @param range the selected range (LOW/MEDIUM/HIGH/VERY_HIGH)
     */
    public void setRange(int range)
    {
        byte cmd = SENSITIVITY_BASE; 
        switch(range)
        {
        case LOW:
        case MEDIUM:
        case HIGH:
        case VERY_HIGH:
            break;
        default:
            throw new IllegalArgumentException("Range setting invalid");
        }
        cmd += range;
        sendData(COMMAND, cmd);
        // update gyro scale to match new setting
        gyroMode.setScale(gyroScale[range]);
    }

    /**
     * Set the smoothing filter for the gyro. <br>
     * The Gyro readings are filtered with n’th order finite impulse response
     * filter, (where n ranges from 0 to 7) 
     * value 0 will apply no filter, resulting in faster reading, 
     * but noisier values.value 7 will apply stronger filter resulting 
     * in slower read (about 10 milli-seconds slower) but less noise.<br>
     * The default value for the filter is 4.
     * @param value (range 0-7)
     */
    public void setGyroFilter(int value)
    {
        sendData(GYRO_FILTER, (byte)value);
    }

    // TODO: Which axis? Which Direction?
    /**
     * Starts calibration for the compass. Must rotate *very* 
     * slowly, taking at least 20 seconds per rotation.
     * 
     * Should make 1.5 to 2 full rotations, around each axis
     * Must call stopCalibration() when done.
     */    @Override
    public void startCalibration()
    {
         sendData(COMMAND, START_CALIBRATION);
    }

     /**
      * Ends calibration sequence.
      *
      */ 
    @Override
    public void stopCalibration()
    {
        sendData(COMMAND, START_CALIBRATION);
    }

}
