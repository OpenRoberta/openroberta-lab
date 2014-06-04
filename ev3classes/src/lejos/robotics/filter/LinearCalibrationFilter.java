package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * This filter is used to calibrate sensors for offset and scale errors using
 * linear interpolation. <br>
 * The filter has two modes of operation. In operational mode it corrects
 * samples coming from the sensor. In calibration mode the filter calculates
 * calibration parameters.
 * <p>
 * 
 * <b>Operational mode</b><br>
 * In operational mode the filter corrects incoming samples for offset and scale
 * errors. The correction algorithm can uses calibration parameters for this.
 * These calibration parameters can be computed in calibration mode. They can
 * also be loaded from the filesystem using the load() method.
 * <p>
 * 
 * <b>How it works</b><br>
 * The correction algorithm uses two calibration parameters, offset and scale.<br>
 * The offset parameter corrects for offset errors. An offset error results in a
 * sample being a fixed amount off the truth: 2 becomes 3, 6 becomes 7. It is
 * corrected by subtracting a constant value (1 in this example) from the
 * sample, the offset error. <br>
 * The scale parameter corrects for scale errors. As a result of a scale error a
 * sample is always a fixed percentage off the truth: 2 becomes 3, 6 becomes 9.
 * It is corrected by dividing the sample with a constant value, the scale
 * error, 1.5 in this case. <br>
 * The combined correction formula is correctedValue = ( rawValue - offsetError
 * ) / scaleError. This calibration technique works on all sensors who's output
 * is linear to the input.<br>
 * If no correction parameters are calculated or loaded the filter uses 0 for
 * offset correction and 1 for scale correction.
 * <p>
 * 
 * <b>How to use the filter in operational mode</b><br>
 * In operational mode is the default mode. It is always active while the filter
 * is not calibrating. <br>
 * <p>
 * 
 * 
 * 
 * <b>Calibration mode</b><br>
 * In calibration mode the two calibration parameters are calculated. This is
 * done by comparing samples to a user specified reference value and/or range.
 * Once calibration parameters are calculated they can be used immediately or
 * stored to the filesystem for later use using the store() method.
 * <p>
 * 
 * <b>How it works</b><br>
 * The CalibratorFiltersupports both offset and scale calibration. However both
 * are optional and can be enabled or disabled. During calibration the filter
 * collects minimum and maximum sample values. From this it calculates offset
 * (as the average of the minimum and maximum value corrected for the reference
 * value) and scale (as the difference between maximum and minimum value scaled
 * for range). To minimize the effect of sensor noise or sensor movements during
 * calculation sample values are low-passed before they are being used for
 * calibration.
 * <p>
 * 
 * <b>How to use the filter in calibration mode</b><br>
 * Calibration is started using the startCalibration method and ended with the
 * endCalibration method. During calibration the program that uses the filter
 * must fetch samples to collect data for the calibration algorithm. At the end
 * the calibration process the calculated calibration settings can be stored
 * using the store(filename) method. Calibration can be paused if needed.
 * <p>
 * 
 * <b>How to tune the calibration process</b><br>
 * There are three important parameters to the calibration process that can be
 * modified by the user program.
 * <ul>
 * <li>
 * The reference value. This is the expected output of the sensor. For
 * calibrating a (motionless) gyro sensor this will be 0. For calibrating a
 * range sensor for example this should be the range to the object the sensor is
 * calibrated to. The reference value is used in calculating the offset
 * parameter, it is not used in calculating scale. The reference has a default
 * value of 0.</li>
 * <li>
 * The range value. This is the expected range of the sensor output. For
 * calibrating an accelerometer this could be 2*9.81 when the output should be
 * in m/s^2. The range value is used in calculating the scale parameter, it is
 * not used in calculating offset. The range has a default value of 2, meaning
 * sample values are normalized to a range of -1 to 1.</li>
 * <li>
 * The timeConstant value. This is the timeConstant value of a low-pass filter
 * that is used in calibration mode. A low pass filter is used during
 * calibration to for two reasons. First to overcome the effects of sensor noise
 * that could otherwise seriously affect range calculation. Secondly it filters
 * out the side effect of user manipulation when turning the sensor as part of
 * the calibration process (six way tumbling method). The parameter affects the
 * amount of smoothing of the low-pass filter. The higher the value, the
 * smoother the samples. Smoother samples are less affected by sensor noise or
 * external shocks but take longer to settle. The time constant has a default
 * value of 0, meaning no smoothing is done by default.</li>
 * </ul>
 * 
 * @author Aswin Bouwmeester
 * 
 */
public class LinearCalibrationFilter extends AbstractCalibrationFilter {
  public final static int OFFSET_CALIBRATION           = 0;
  public final static int OFFSET_AND_SCALE_CALIBRATION = 1;

  private float[]         lowerBound;
  private float[]         upperBound;
  private float[]         offset;
  private float[]         scale;
  private int             calibrationType;

  /**
   * Construcor
   * 
   * @param source
   *          SampleProvider
   * @param filename
   *          Filename to load calibration settings from
   */
  public LinearCalibrationFilter(SampleProvider source, String filename) {
    this(source);
    open(filename);
  }

  public LinearCalibrationFilter(SampleProvider source) {
    super(source);
    lowerBound = new float[sampleSize];
    upperBound = new float[sampleSize];
    offset = new float[sampleSize];
    scale = new float[sampleSize];

    for (int i = 0; i < sampleSize; i++) {
      upperBound[i] = 0;
      lowerBound[i] = 0;
      offset[i] = 0;
      scale[i] = 1;
    }
  }

  public String toString() {
    return "LinearCalibrationFilter";
  }

  public void setScaleCalibration(float ulBound) {
    for (int i = 0; i < sampleSize; i++) {
      lowerBound[i] = -ulBound;
      upperBound[i] = ulBound;
    }
  }

  public void setScaleCalibration(float lBound, float uBound) {
    for (int i = 0; i < sampleSize; i++) {
      lowerBound[i] = lBound;
      upperBound[i] = uBound;
    }
  }

  public void setScaleCalibration(float[] lBound, float[] uBound) {
    for (int i = 0; i < sampleSize; i++) {
      lowerBound[i] = lBound[i];
      upperBound[i] = uBound[i];
    }
  }

  public void setOffsetCalibration(float offset) {
    for (int i = 0; i < sampleSize; i++) {
      upperBound[i] = offset;
      lowerBound[i] = offset;
    }
  }

  public void setOffsetCalibration(float[] offset) {
    for (int i = 0; i < sampleSize; i++) {
      upperBound[i] = offset[i];
      lowerBound[i] = offset[i];
    }
  }

  public void setCalibrationType(int calibrationType) {
    if (calibrationType < 0 || calibrationType > 1)
      throw new IllegalArgumentException();
    this.calibrationType = calibrationType;
  }

  public int getCallibrationType() {
    return calibrationType;
  }

  /**
   * Returns an array with the offset correction parameters that are currently
   * in use
   * 
   * @return the offset correction array
   */
  public float[] getOffsetCorrection() {
    float[] ret = new float[sampleSize];
    System.arraycopy(offset, 0, ret, 0, sampleSize);
    return ret;
  }

  /**
   * Returns an array with the scale correction paramaters that are currently in
   * use
   * 
   * @return the scale correction array
   */
  public float[] getScaleCorrection() {
    float[] ret = new float[sampleSize];
    System.arraycopy(scale, 0, ret, 0, sampleSize);
    return ret;
  }

  /**
   * Starts a calibration process. Resets collected minimum and maximum values.
   * After starting calibration new minimum and maximum values are calculated on
   * each fetched sample. From this updated offset and scale parameters are
   * calculated.
   */
  @Override
  public void startCalibration() {
    super.startCalibration();
    reset();
  }

  @Override
  public void stopCalibration() {
    super.stopCalibration();
    System.out.println("End calibration using " + numberOfSamplesInCalibration + " samples.");
    for (int i = 0; i < sampleSize; i++) {
      System.out.println("min: " + min[i] + " max: " + max[i] + " sum: " + sum[i] + " lowerbound: " + lowerBound[i] + " upperbound: " + upperBound[i] + " offset: " + offset[i] + "scale: " + scale[i]);
    }
  }

  /**
   * Stores the calibration parameters, offset and/or scale depending on current
   * settings, to a filterProperties file. Stored parameters can later be used
   * by the CalibrationFilter.
   * 
   * @param filename
   *          A name to use for storing calibration parameters
   */
  public void save(String filename) {
    setPropertyArray("offset", offset);
    setPropertyArray("scale", scale);
    setProperty("calibrationType", (float) calibrationType);
    store(filename);
  }

  public void open(String name) {
    reset();
    load(name);
    offset = getPropertyArray("offset");
    scale = getPropertyArray("scale");
    calibrationType = (int) getProperty("calibrationType");
  }

  private void reset() {
    for (int i = 0; i < sampleSize; i++) {
      offset[i] = 0;
      scale[i] = 1;
    }
  }

  /**
   * Fetches a sample from the sensor and updates calibration parameters when
   * the calibration process is running.
   */
  public void fetchSample(float[] dst, int off) {
    super.fetchSample(dst, off);
    for (int i = 0; i < sampleSize; i++) {
      if (calibrating) {
        if (upperBound[i] != lowerBound[i])
          scale[i] = (max[i] - min[i]) / (upperBound[i] - lowerBound[i]);
        offset[i] = (max[i] + min[i])/2 - (upperBound[i] + lowerBound[i])/2;
      }
      else {
        dst[i + off] = (dst[i + off] - offset[i]);
        if (calibrationType == OFFSET_AND_SCALE_CALIBRATION)
          dst[i + off] /= scale[i];
      }
    }

  }

  public int getCalibrationType() {
    return calibrationType;
  }

}
