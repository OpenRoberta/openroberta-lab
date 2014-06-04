package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.SampleProvider;

/**
 * Sensor driver for the Lego EV3 Gyro sensor.
 * <OL>
 * This sensor supports three modes:
 * <li>Rate mode, provides the rate of turn in degrees per second</li>
 * <li>Angle mode, provides the accumulated angle in degrees</li>
 * <li>Rate and Angle mode, provides both of the previous</li>
 * </OL>
 * A positive angle or rate indicates a counter clockwise rotation. <br>
 * Use reset() to recalibrate the sensor and to reset accumulated angle to zero. 
 * 
 * @author Andy, Aswin Bouwmeester
 */
public class EV3GyroSensor extends UARTSensor {
  private static final long SWITCHDELAY = 200;
  private short[] raw=new short[2];

  public EV3GyroSensor(Port port) {
    super(port);
    setModes(new SensorMode[] { new RateMode(), new AngleMode(), new RateAndAngleMode() });
  }

  public EV3GyroSensor(UARTPort port) {
    super(port);
  }

  /**
   * Returns an SampleProvider object representing the gyro sensor in angle
   * mode. <br>
   * In rate mode the sensor measures the orientation of the sensor in repect to
   * its start position. A positive angle indicates a orientation to the left. A
   * negative rate indicates a rotation to the right. Angles are expressed in
   * degrees.<br>
   */
  public SampleProvider getAngleMode() {
    return getMode(1);
  }

  /**
   * Returns an SampleProvider object representing the gyro sensor in rate mode. <br>
   * In rate mode the sensor measures the speed of rotation expressed in
   * degrees/second. A positive rate indicates a counterclockwise rotation. A
   * negative rate indicates a clockwise rotation.
   */
  public SampleProvider getRateMode() {
    return getMode(0);
  }

  /**
   * Returns an SampleProvider object representing the gyro sensor in both rate and mode. <br>
   * In this mode the sensor measures both the speed of rotation expressed (degrees/second) and the 
   * accumulated angle (degrees). A positive rate or angle  indicates a counterclockwise rotation. A
   * negative rate indicates a clockwise rotation. <br>
   * <OL>The sample contains: 
   * <li> Accumulated angle in degrees</li>
   * <li> Rate in degrees/second</li>
   * </ol>
   */
  public SampleProvider getAngleAndRateMode() {
    return getMode(2);
  }

  
  /**
   * Hardware calibration of the Gyro sensor and reset off accumulated angle to zero. <br>
   * The sensor should be motionless during calibration.
   */
  public void reset() {
    // Reset mode (4) is not used here as it behaves eratically. Instead the reset is done implicitly by going to mode 1.
    switchMode(1, SWITCHDELAY);
  }

  private class AngleMode implements SampleProvider, SensorMode {
    private static final int   MODE = 3;
    private static final float toSI = -1;

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      port.getShorts(raw, 0, raw.length);
      sample[offset] = raw[0] * toSI;
    }

    @Override
    public String getName() {
      return "Angle";
    }

  }

  private class RateMode implements SampleProvider, SensorMode {
    private static final int   MODE = 3;
    private static final float toSI = -1;

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      port.getShorts(raw, 0, raw.length);
      sample[offset] = raw[1] * toSI;
    }

    @Override
    public String getName() {
      return "Rate";
    }

  }

  private class RateAndAngleMode implements SampleProvider, SensorMode {
    private static final int   MODE = 3;
    private static final float toSI = -1;

    @Override
    public int sampleSize() {
      return 2;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      port.getShorts(raw, 0, raw.length);
      for (int i=0;i<raw.length;i++) {
        sample[offset+i] = raw[i] * toSI;
      }
    }

    @Override
    public String getName() {
      return "Angle and Rate";
    }

  }

  
}
