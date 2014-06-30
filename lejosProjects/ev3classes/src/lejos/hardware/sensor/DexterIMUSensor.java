package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lejos.utility.EndianTools;

/**
 * Represents the Dexter Industries dIMU sensor.<br>
 * The IMU sensor provider acceleration (m/s), rate (degrees/sec) and
 * temperature (Celcius) data. <br>
 * <a href="http://dexterindustries.com/files/dIMU_Datasheets.zip">IMU sensor
 * datasheets</a>
 * 
 * @author Aswin
 * 
 */
public class DexterIMUSensor extends BaseSensor implements SensorModes {
  // TODO: Add support for sensor configuration

  // I2C Addresses for the gyro and acceleration chips with the default values
  protected int Accel_I2C_address = 0x3A;
  protected int Gyro_I2C_address  = 0xD2;

  public DexterIMUSensor(I2CPort port) {
    DexterIMUGyroSensor gyro = new DexterIMUGyroSensor(port, Gyro_I2C_address);
    setModes(new SensorMode[] { gyro.getMode(0), new DexterIMUAccelerationSensor(port, Accel_I2C_address), gyro.getMode(1) });
  }

  public DexterIMUSensor(Port port) {
    DexterIMUGyroSensor gyro = new DexterIMUGyroSensor(port, Gyro_I2C_address);
    setModes(new SensorMode[] { gyro.getMode(0), new DexterIMUAccelerationSensor(gyro.port, Accel_I2C_address), gyro.getMode(1) });
  }

  /**
   * Gives a SampleProvider thst returns acceleration (m/s) samples.
   * 
   * @return a SampleProvider
   */
  public SampleProvider getAccelerationMode() {
    return getMode(1);
  }

  /**
   * Gives a SampleProvider that returns rate (degree/s) samples.
   * 
   * @return a SampleProvider
   */
  public SampleProvider getRateMode() {
    return getMode(0);
  }

  /**
   * Gives a SampleProvider thst returns temperature (degree Celcius) samples.<br>
   * Note that this is the temperature of the gyro chip. This does not have to
   * correspond to the temperture of the surrounding.
   * 
   * @return a SampleProvider
   */
  public SampleProvider getTemperatureMode() {
    return getMode(2);
  }

  /**
   * Provides access to the gyro sensor on the IMU. <br>
   * The Gyro sensor is the L3G4200D from ST.
   * 
   * @author Aswin
   * 
   */
  private class DexterIMUGyroSensor extends I2CSensor {

    // Register adresses
    private static final int CTRL_REG1   = 0x020;
    private static final int CTRL_REG2   = 0x021;
    private static final int CTRL_REG3   = 0x022;
    private static final int CTRL_REG4   = 0x023;
    private static final int CTRL_REG5   = 0x024;
    private static final int REG_STATUS  = 0x27;

    // Configurable parameters
    // This sensor can configured for samplerate and range. Currently only the
    // default values are set.
    // The code to set other values is there.

    // private float[] RATES = { 100,200,400,800};
    private int[]            RATECODES   = { 0x00, 0x40, 0x80, 0xC0 };
    // private float[] RANGES = { 250,500,2000};
    private int[]            RANGECODES  = { 0x00, 0x10, 0x20 };
    private float[]          MULTIPLIERS = { 8.75f, 17.5f, 70f };

    private int              range       = 2;
    private int              rate        = 0;
    private float            toSI        = 1f/MULTIPLIERS[range] ;

    private byte[]           buf         = new byte[7];

    public DexterIMUGyroSensor(I2CPort port, int address) {
      super(port, address);
      init();
    }

    public DexterIMUGyroSensor(Port port, int address) {
      super(port, address, TYPE_LOWSPEED_9V);
      init();
    }

    /**
     * This method configures the sensor
     */
    private void init() {
      setModes(new SensorMode[] { new RateMode(), new TemperatureMode() });
      int reg;
      // put in sleep mode;
      sendData(CTRL_REG1, (byte) 0x08);
      // oHigh-pass cut off 1 Hz;
      sendData(CTRL_REG2, (byte) 0x01);
      // no interrupts, no fifo
      sendData(CTRL_REG3, (byte) 0x08);
      // set range
      reg = RANGECODES[range] | 0x80;
      toSI = MULTIPLIERS[range] / 1000f;
      sendData(CTRL_REG4, (byte) reg);
      // disable fifo and high pass
      sendData(CTRL_REG5, (byte) 0x00);
      // stabilize output signal;
      // enable all axis, set output data rate ;
      reg = RATECODES[rate] | 0xF;
      // set sample rate, wake up
      sendData(CTRL_REG1, (byte) reg);
      float[] dummy = new float[3];
      SampleProvider gyro = getGyroMode();
      for (int s = 1; s <= 15; s++) {
//        while (!isNewDataAvailable())
//          Thread.yield();
        gyro.fetchSample(dummy, 0);
      }
    }

    /**
     * Returns true if new data is available from the sensor
     */
    @SuppressWarnings("unused")
    private boolean isNewDataAvailable() {
      getData(REG_STATUS, buf, 1);
      if ((buf[0] & 0x08) == 0x08)
        return true;
      return false;
    }

    /**
     * Provides access to the temperature data on the Gyro sensor
     * Method added for consistency with other SensoModes
     * 
     * @return
     */
    @SuppressWarnings("unused")
    public SampleProvider getTemperatureMode() {
      return getMode(1);
    }

    /**
     * Provides access to the rate data on the gyro sensor
     * 
     * @return
     */
    public SampleProvider getGyroMode() {
      return getMode(0);
    }

    /**
     * Represent the gyro sensor in temperature mode
     * 
     * @author Aswin
     * 
     */
    private class TemperatureMode implements SampleProvider, SensorMode {
      private static final int DATA_REG = 0x26 | 0x80;

      @Override
      public int sampleSize() {
        return 1;
      }

      @Override
      public void fetchSample(float[] sample, int offset) {
        // Temperature in degrees
        getData(DATA_REG, buf, 1);
        sample[offset] = buf[0];
      }

      @Override
      public String getName() {
        return "Temperature";
      }

    }

    /**
     * Represent the gyro sensor in rate mode
     * 
     * @author Aswin
     * 
     */
    private class RateMode implements SampleProvider, SensorMode {
      private static final int DATA_REG = 0x27 | 0x80;

      @Override
      public int sampleSize() {
        return 3;
      }



      @Override
      public void fetchSample(float[] sample, int offset) {
        buf[0] = 0;
        getData(DATA_REG, buf, 7);
          // a correction for misalignment of the gyro sensor is made here
          sample[offset] = EndianTools.decodeShortLE(buf, 3) * toSI;
          sample[1 + offset] = EndianTools.decodeShortLE(buf, 1) * toSI;
          sample[2 + offset] = EndianTools.decodeShortLE(buf, 5) * toSI;
      }

      
      @Override
      public String getName() {
        return "Rate";
      }
    }
  }

  /**
   * Class to access the Acceleration sensor from Dexter Industries IMU the
   * acceleration sensor is the Freescale MMA7455
   * 
   * @author Aswin
   * 
   */
  public class DexterIMUAccelerationSensor extends I2CSensor implements SampleProvider, SensorMode {
    protected static final int   DATA_10BIT_REG = 0x00;
    protected static final int   DATA_8BIT_REG  = 0x06;
    protected static final int   MODE_REG       = 0x16;
    protected static final float TOSI           = 100f / (64.0f * 9.81f);

    private byte[]               buf            = new byte[6];

    private DexterIMUAccelerationSensor(I2CPort port, int address) {
      super(port, address);
      // configuure for 2G measurement mode
      sendData(MODE_REG, (byte) 0x05);
    }

    @Override
    public int sampleSize() {
      return 3;
    }

    private void fetchSample10(float[] sample, int offset) {
      getData(DATA_10BIT_REG, buf, 6);
      for (int i = 0; i < 3; i++) {
         buf[i*2+1] = (byte)((byte)(buf[i*2+1] << 6) >> 6);
         sample[i + offset] = EndianTools.decodeShortLE(buf, i*2);
         sample[i + offset] *= TOSI;
      }
    }

    @SuppressWarnings("unused")
    private void fetchSample8(float[] sample, int offset) {
      getData(DATA_8BIT_REG, buf, 3);
      for (int i = 0; i < 3; i++) {
        sample[i + offset] = buf[i] * TOSI;
      }
    }

    @Override
    public String getName() {
      return "Acceleration";
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      fetchSample10(sample, offset);
    }
  }

}
 