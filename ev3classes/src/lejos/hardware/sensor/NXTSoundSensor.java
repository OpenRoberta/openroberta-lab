package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

/**
 * Abstraction for a NXT sound sensor.
 * 
 */
public class NXTSoundSensor extends AnalogSensor implements SensorConstants, SensorMode {
  protected static final long SWITCH_DELAY = 10;
  /**
   * Create a sound sensor.
   * 
   * @param port
   *          the sensor port to use
   */
  public NXTSoundSensor(Port port) {
    super(port);
    init();
  }

  /**
   * Create a sound sensor.
   * 
   * @param port
   *          the sensor port to use
   */
  public NXTSoundSensor(AnalogPort port) {
    super(port);
    init();
  }

  private void init() {
    setModes(new SensorMode[]{ this, new DBMode() }); 
  }

  public class DBMode implements  SensorMode {

    @Override
    public int sampleSize() {
      return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset) {
      switchType(TYPE_SOUND_DB, SWITCH_DELAY);
      sample[offset] = 1.0f - normalize(port.getPin1());
    }

    @Override
    public String getName() {
      return "Sound DB";
    }

  }

  public SampleProvider getDBMode() {
    return getMode(1);
  }

  public SampleProvider getDBAMode() {
    return getMode(0);
  }

  @Override
  public int sampleSize() {
    return 1;
  }

  @Override
  public void fetchSample(float[] sample, int offset) {
    switchType(TYPE_SOUND_DBA, SWITCH_DELAY);
    sample[offset] =  1.0f - normalize(port.getPin1());
  }

  @Override
  public String getName() {
    return "Sound DBA";
  }
}
