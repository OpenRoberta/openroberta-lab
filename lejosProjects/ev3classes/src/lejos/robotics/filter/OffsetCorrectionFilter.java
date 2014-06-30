package lejos.robotics.filter;

import java.util.LinkedList;
import java.util.Queue;

import lejos.robotics.SampleProvider;

/**
 * The OffsetCorrectionFilter is used to correct sensors that have an unknown
 * offset error.<br>
 * The offset error is calculated by this class and then substracted from the
 * sample to give a corrected sample. The filter works by calculating a running
 * mean of a sample. this mean value is compared to the reference value, the
 * difference between the two is the offset error. Not all samples are used to
 * calculate the running mean. Only those samples that seem to indicate a stable
 * (initial) situation are used for calculating the mean.
 * 
 * @author Aswin
 * 
 */
public class OffsetCorrectionFilter extends AbstractFilter {
  float[]                offset;
  float[]                reference;
  private float[]        mean;
  private float[]        m2;
  private float[]        actual;
  private Queue<Float>[] buffer;
  private int            bufferSize;

  /**
   * Constructor for the offset correction filter using default parameters. <br>
   * All samples are corrected to a reference value of zero using a window of
   * 200 samples for calculating the mean
   * 
   * @param source
   *          source for sample
   */
  public OffsetCorrectionFilter(SampleProvider source) {
    this(source, new float[source.sampleSize()]);
  }

  /**
   * Constructor for the offset correction filter using default window of 200
   * samples. <br>
   * All samples are corrected to the specified reference values
   * 
   * @param source
   *          source for sample
   * @param reference
   *          An array with reference values. The array length should match the
   *          sample size.
   */
  public OffsetCorrectionFilter(SampleProvider source, float[] reference) {
    this(source, reference, 200);
  }

  /**
   * Constructor for the offset correction filter.<br>
   * Constructor
   * 
   * @param source
   *          Source for sample
   * @param reference
   *          An array with reference values. The array length should match the
   *          sample size.
   * @param bufferSize
   *          Number of samples to use for calculating offset error
   */
  @SuppressWarnings("unchecked")
  public OffsetCorrectionFilter(SampleProvider source, float[] reference, int bufferSize) {
    super(source);
    this.bufferSize = bufferSize;
    offset = new float[sampleSize];
    this.reference = reference;
    mean = new float[sampleSize];
    m2 = new float[sampleSize];
    actual = new float[sampleSize];
    buffer = new Queue[sampleSize];
    for (int i = 0; i < sampleSize; i++) {
      buffer[i] = new LinkedList<Float>();
    }
  }

  public void fetchSample(float[] sample, int offset) {
    super.fetchSample(actual, 0);
    updateStatistics();
    for (int i = 0; i < sampleSize; i++) {
      sample[i + offset] = actual[i] - mean[i] + reference[i];
    }
  }

  private void updateStatistics() {
    for (int i = 0; i < sampleSize; i++) {
      if (withinLimits(i)) {
        if (buffer[i].size() == bufferSize) {
          removeSample(i);
        }
        addSample(i);
      }
    }
  }

  private boolean withinLimits(int i) {
    if (actual[i] == Float.NaN)
      return false;
    if (buffer[i].size() < 15)
      return true;
    float interval = 2 * this.getStandardDeviation(i);
    if (actual[i] < mean[i] - interval)
      return false;
    if (actual[i] > mean[i] + interval)
      return false;
    return true;
  }

  /**
   * Method to maintain the running mean and variance by adding a new value;
   * 
   * @param i
   */
  private void addSample(int i) {
    float x = actual[i];
    buffer[i].add(new Float(x));
    float delta = x - mean[i];
    mean[i] += delta / buffer[i].size();
    m2[i] += delta * (x - mean[i]);
  }

  /**
   * Method to maintain the running mean and variance by removing an old value;
   * 
   * @param i
   */
  private void removeSample(int i) {
    float x = (Float) buffer[i].poll();
    float delta = x - mean[i];
    mean[i] -= delta / buffer[i].size();
    ;
    m2[i] -= delta * (x - mean[i]);
  }

  @SuppressWarnings("unused")
  private float getVariance(int i) {
    return m2[i] / (buffer[i].size() - 1);
  }

  private float getStandardDeviation(int i) {
    return (float) Math.sqrt(m2[i] / (buffer[i].size() - 1));
  }

  /**
   * Returns the mean sample value
   * 
   * @return
   */
  public float[] getMean() {
    return mean.clone();
  }

  /**
   * Returns the standard deviation from the mean sample value
   * 
   * @return
   */
  public float[] getStandardDeviation() {
    float[] std = new float[sampleSize];
    for (int i = 0; i < sampleSize; i++) {
      if (buffer[i].size() < 2)
        std[i] = Float.NaN;
      else
        std[i] = getStandardDeviation(i);
    }
    return std;
  }

}
