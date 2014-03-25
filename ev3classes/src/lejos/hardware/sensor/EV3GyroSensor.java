package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.SampleProvider;

/**
 * Sensor driver for the Lego EV3 Gyro sensor.<br>
 * Please note that the Gyro sensor that the sensor can supply both rate and
 * angle. Everytime your program switches from rate to angle the sensor resets
 * automaticly and therefore should by motionless during this operation.
 * 
 * @author Andy, Aswin Bouwmeester
 */
public class EV3GyroSensor extends UARTSensor {
  private static final long SWITCHDELAY = 200;
  private static final int  RESETMODE   = 4;

  public EV3GyroSensor(Port port) {
    super(port);
    setModes(new SensorMode[] { new RateMode(), new AngleMode() });
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
   * Hardware calibration of the Gyro sensor. <br>
   * The sensor should be motionless during calibration.
   */
  public void reset() {
    // TODO: Test if angle is reset to zero due to calibration
    // TODO: find out how to get out of calibration mode
    switchMode(RESETMODE, SWITCHDELAY);
  }

  private class AngleMode implements SampleProvider, SensorMode {
    private static final int   MODE = 0;
    private static final float toSI = -1;

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      sample[offset] = port.getShort() * toSI;
    }

    @Override
    public String getName() {
      return "Angle";
    }

  }

  private class RateMode implements SampleProvider, SensorMode {
    private static final int   MODE = 1;
    private static final float toSI = -1;

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      sample[offset] = port.getShort() * toSI;
    }

    @Override
    public String getName() {
      return "Rate";
    }

  }

}
