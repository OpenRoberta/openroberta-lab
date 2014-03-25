package lejos.robotics;

import lejos.robotics.filter.IntegrationFilter;
import lejos.robotics.filter.LinearCalibrationFilter;
import lejos.robotics.filter.SampleThread;
import lejos.utility.Delay;

/**
 * Provides an adapter that implements the Gyroscope interface. <br>
 * The gyroscope is sampled at a regular interval and its output is integrated into accumulated angle.
 * @author Aswin
 *
 */
public class GyroscopeAdaptor implements Gyroscope {
  SampleProvider original,   sampler;
  LinearCalibrationFilter calibrator;
  IntegrationFilter integrator;
  float[] buffer;
  int index=0;
  
  
  /**
   * @param source
   * A SampleProvider representing a gyroscope
   * @param sampleFrequency
   * The frequency used to sample the gyroscope
   * @param axisIndex
   * The axis to use (usefull with multiaxis gyroscopes
   */
  public GyroscopeAdaptor(SampleProvider source, float sampleFrequency, int axisIndex) {
    original=source;
    calibrator=new LinearCalibrationFilter(original);
    integrator=new IntegrationFilter(calibrator);
    sampler=new SampleThread(integrator, sampleFrequency);
    calibrator.setOffsetCalibration(0);
    calibrator.setCalibrationType(LinearCalibrationFilter.OFFSET_CALIBRATION);
    buffer=new float[original.sampleSize()];
    index=axisIndex;
  }
  
  /**
   * @param source
   * A SampleProvider representing a gyroscope
   * @param sampleFrequency
   * The frequency used to sample the gyroscope
   */
  public GyroscopeAdaptor(SampleProvider source, float sampleFrequency) {
    this(source, sampleFrequency, 0);
  }

  @Override
  public float getAngularVelocity() {
    calibrator.fetchSample(buffer, index);
    return buffer[0];
  }


  /** 
   * Racalibrates the gyroscope for offset error. <br>
   * Calibration takes a second during which the gyroscope must remain motionless.
   */
  public void recalibrateOffset() {
    calibrator.startCalibration();
    Delay.msDelay(1000);
    calibrator.stopCalibration();
  }

  @Override
  public int getAngle() {
    sampler.fetchSample(buffer, index);
    return (int)buffer[0];
  }

  @Override
  public void reset() {
    integrator.resetTo(0);
  }

}
