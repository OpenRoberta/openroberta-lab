package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import lejos.utility.EndianTools;

/**
 * Driver for the Dexter Industries compass sensor. <br>
 * This sensor uses the Honeywell HMC5883L 3-Axis digital compass IC.
 * 
 * @author Aswin
 * @version 1.0
 * 
 */
public class DexterCompassSensor extends I2CSensor implements SensorMode {

  // sensor configuration
  static final int             MODE_NORMAL        = 0;
  static final int             MODE_POSITIVE_BIAS = 1;
  static final int             MODE_NEGATIVE_BIAS = 2;
  private final static float[] RATES              = { 0.75f, 1.5f, 3, 7.5f, 15, 30, 75 };
  private final static int[]   RANGEMULTIPLIER    = { 1370, 1090, 820, 660, 440, 390, 330, 230 };
  private final static float[] RANGES             = { 0.88f, 1.3f, 1.9f, 2.5f, 4, 4.7f, 5.6f, 8.1f };
  static final int             CONTINUOUS         = 0;
  static final int             SINGLE             = 1;
  static final int             IDLE               = 2;

  // default configuration
  int                          measurementMode    = MODE_NORMAL;
  int                          range              = 6;
  int                          rate               = 5;
  int                          operatingMode      = CONTINUOUS;

  // sensor register adresses
  private static final int     I2C_ADDRESS        = 0x3C;
  protected static final int   REG_CONFIG         = 0x00;
  protected static final int   REG_MAGNETO        = 0x03;
  protected static final int   REG_STATUS         = 0x09;

  // local variables for common use
  float[]                      raw                = new float[3];
  float[]                      dummy              = new float[3];
  byte[]                       buf                = new byte[6];
  private float                multiplier;

  /**
   * Constructor for the driver. 
   * 
   * @param port
   */
  public DexterCompassSensor(I2CPort port) {
    super(port, I2C_ADDRESS);
    init();
  }

  public DexterCompassSensor(Port port) {
    super(port, I2C_ADDRESS,  TYPE_LOWSPEED_9V);
    init();
  }

  protected void init() {
    setModes(new SensorMode[] { this });
    configureSensor();
  }

  /**
   * Sets the configuration registers of the sensor according to the current
   * settings
   */
  private void configureSensor() {
    // TODO: remove debug code
    System.out.println("rate: "+rate);
    System.out.println("measurementMode: "+measurementMode);
    System.out.println("range: "+range);
    System.out.println("operatingMode: "+operatingMode);
    
    buf[0] = (byte) ((3 << 5) | (rate << 2) | measurementMode);
    buf[1] = (byte) (range << 5);
    buf[2] = (byte) (operatingMode);
    
    // TODO: remove debug code
    System.out.println("Buffer to send");
    for(int i=0;i<3;i++) {
      System.out.println(String.format("%8s", Integer.toBinaryString(buf[i] & 0xFF)).replace(' ', '0'));
    }
    
    sendData(REG_CONFIG, buf, 3);
    
    // TODO: remove debug code
    Delay.msDelay(200);
    System.out.println("Actual value of registers");
    getData(REG_CONFIG, buf, 3);
    for(int i=0;i<3;i++) {
      System.out.println(String.format("%8s", Integer.toBinaryString(buf[i] & 0xFF)).replace(' ', '0'));
    }
    
    Delay.msDelay(6);
    
    multiplier = 1.0f / RANGEMULTIPLIER[range];
    // first measurement after configuration is not yet configured properly;
    Delay.msDelay(6);
    fetchSample(dummy, 0);
  }

  /**
   * Fills an array of floats with measurements from the sensor in the specified
   * unit.
   * <p>
   * The array order is X, Y, Z
   * <P>
   * When the sensor is idle zeros will be returned.
   */
  public void fetchSample(float[] sample, int offset) {
    // get raw data
    switch (operatingMode) {
      case (SINGLE):
        fetchSingleMeasurementMode(sample, offset);
        break;
      case (CONTINUOUS):
        fetch(sample, offset);
        break;
      default:
        for (int axis = 0; axis < 3; axis++)
          sample[axis + offset] = Float.NaN;
        break;
    }
  }

  /**
   * Returns the raw values from the data registers of the sensor
   * 
   * @param ret
   */
  private void fetch(float[] ret, int offset) {
    // The order of data registers seems to be X,Z,Y. (Aswin).
    getData(REG_MAGNETO, buf, 6);
    ret[0 + offset] = EndianTools.decodeShortBE(buf, 0) * multiplier;
    ret[1 + offset] = EndianTools.decodeShortBE(buf, 4) * multiplier;
    ret[2 + offset] = EndianTools.decodeShortBE(buf, 2) * multiplier;
  }

  /**
   * fetches measurement in single measurement mode
   * 
   * @param ret
   */
  private void fetchSingleMeasurementMode(float[] ret, int offset) {
    buf[0] = 0x01;
    sendData(0x02, buf[0]);
    Delay.msDelay(6);
    fetch(ret, offset);
  }

  /**
   * @return Returns the measurement mode of the sensor (normal, positive bias
   *         or negative bias).
   *         <p>
   *         positive and negative bias mode should only be used for testing the
   *         sensor.
   */
  public int getMeasurementMode() {
    return measurementMode;
  }

  /**
   * @return The operating mode of the sensor (single measurement, continuous or
   *         Idle)
   */
  public int getOperatingMode() {
    return operatingMode;
  }

  /**
   * @return The dynamic range of the sensor.
   */
  public float getMaximumRange() {
    return RANGES[range];
  }

  @Override
  public String getProductID() {
    return "DiCompass";
  }

  @Override
  public String getVendorID() {
    return "Dexter";
  }

  @Override
  public String getVersion() {
    return "1.0";
  }

  /**
   * Reads the new data ready bit of the status register of the sensor.
   * 
   * @return True if new data available
   */
  public boolean newDataAvailable() {
    getData(REG_STATUS, buf, 1);
    return ((buf[0] & 0x01) != 0);
  }

  /**
   * @param measurementMode
   *          Sets the measurement mode of the sensor.
   */
  protected void setMeasurementMode(int measurementMode) {
    this.measurementMode = measurementMode;
    configureSensor();
  }

  /**
   * Sets the operating mode of the sensor
   * 
   * @param operatingMode
   *          Continuous is normal mode of operation
   *          <p>
   *          SingleMeasurement can be used to conserve energy or to increase
   *          maximum measurement rate
   *          <p>
   *          Idle is to stop the sensor and conserve energy
   */
  public void setOperatingMode(int operatingMode) {
    this.operatingMode = operatingMode;
    configureSensor();
  }

  /**
   * Sets the dynamic range of the sensor (1.3 Gauss is default).
   * 
   * @param range
   */
  public void setRange(int range) {
    this.range = (byte) range;
    configureSensor();
  }

  /**
   * Self-test routine of the sensor.
   * 
   * @return An array of boolean values. A true indicates the sensor axis is
   *         working properly.
   */
  public boolean[] test() {
    boolean[] ret = new boolean[3];

    // store current settings;
    int currentMode = measurementMode;
    int currentRange = range;
    int currentOperatingMode = operatingMode;

    // modify settings for testing;
    measurementMode = MODE_POSITIVE_BIAS;
    range = 5;
    operatingMode = SINGLE;
    configureSensor();

    // get measurement
    buf[0] = 0x01;
    sendData(0x02, buf[0]);
    Delay.msDelay(6);
    fetch(dummy, 0);

    // test for limits;
    for (int axis = 0; axis < 3; axis++)
      if (dummy[axis] > 243 && dummy[axis] < 575)
        ret[axis] = true;
      else
        ret[axis] = false;

    // restore settings;
    measurementMode = currentMode;
    range = currentRange;
    operatingMode = currentOperatingMode;
    configureSensor();

    return ret;
  }

  public int sampleSize() {
    return 3;
  }

  public float fetchSample() {
    fetchSample(dummy, 0);
    return dummy[0];
  }

  public void setSampleRate(float rate) {
    for (int i = 0; i < RATES.length; i++)
      if (RATES[i] == rate)
        rate = i;
    configureSensor();
  }

  public float[] getSampleRates() {
    return RATES;
  }

  public void start() {
    this.setOperatingMode(CONTINUOUS);
  }

  public void stop() {
    this.setOperatingMode(IDLE);
  }

  public float getSampleRate() {
    return RATES[rate];
  }

  public void setRange(float range) {
    for (int i = 0; i < RANGES.length; i++)
      if (RANGES[i] == range)
        range = i;
    configureSensor();
  }

  public float[] getRanges() {
    return RANGES;
  }

  @Override
  public String getName() {
    return "Magnetic";
  }

}
