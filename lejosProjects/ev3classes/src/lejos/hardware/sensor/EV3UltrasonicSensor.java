package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.SampleProvider;

/**
 * Sensor driver for the Lego EV3 Ultrasonic sensor.<br>
 * 
 * @author Aswin Bouwmeester
 * 
 */
public class EV3UltrasonicSensor extends UARTSensor {

  private static final int DISABLED    = 3;
  private static final int SWITCHDELAY = 200;

  protected void init()
  {
    setModes(new SensorMode[] { new DistanceMode(), new ListenMode() });      
  }
  /**
   * Create the Ultrasonic sensor class.
   * 
   * @param port
   */
  public EV3UltrasonicSensor(Port port) {
    super(port, 0);
    init();
  }

  /**
   * Create the Ultrasonic sensor class.
   * 
   * @param port
   */
  public EV3UltrasonicSensor(UARTPort port) {
    super(port, 0);
    init();
  }

  public SampleProvider getListenMode() {
    return getMode(1);
  }

  public SampleProvider getDistanceMode() {
    return getMode(0);
  }

  /**
   * Enable the sensor. This puts the indicater LED on.
   */
  public void enable() {
    switchMode(0, SWITCHDELAY);
  }

  /**
   * Disable the sensor. This puts the indicater LED off.
   */
  public void disable() {
    switchMode(DISABLED, SWITCHDELAY);
  }

  /**
   * Indicate that the sensor is enabled.
   * 
   * @return True, when the sensor is enabled. <br>
   *         False, when the sensor is disabled.
   */
  public boolean isEnabled() {
    return (currentMode == DISABLED) ? false : true;
  }

  /**
   * Represents a Ultrasonic sensor in distance mode
   */
  private class DistanceMode implements SampleProvider, SensorMode {
    private static final int   MODE = 0;
    private static final float toSI = 0.001f;

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      int raw = port.getShort();
      sample[offset] = (raw == 2550) ? Float.POSITIVE_INFINITY : (float) raw * toSI;
    }

    @Override
    public String getName() {
      return "Distance";
    }

  }

  /**
   * Represents a Ultrasonic sensor in listen mode
   */
  private class ListenMode implements SampleProvider, SensorMode {
    private static final int MODE = 2;

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchMode(MODE, SWITCHDELAY);
      sample[offset] = port.getShort() & 0xff;
    }

    @Override
    public String getName() {
      return "Listen";
    }
  }

}
