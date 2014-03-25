package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

// TODO: think about the build up of numerical errors over time

/**
 * This filter returns the sum of the N most recent samples.<br>
 * The number of samples used is specified in the constructor of the filter.<b>
 * 
 * @author Aswin
 * 
 */
public class SumFilter extends SampleBuffer {
  float[] sum;
  float[] buf;

  public SumFilter(SampleProvider source, int length) {
    super(source, length);
    sum = new float[sampleSize];
    buf = new float[sampleSize];
  }

  @Override
  public void fetchSample(float[] sample, int off) {
    // substract oldest sample from sum
    getOldest(buf, 0);
    for (int i = 0; i < sampleSize; i++) {
      sum[i] -= buf[i];
    }
    super.fetchSample(buf, 0);
    // add the newest sample to sum
    for (int i = 0; i < sampleSize; i++) {
      sum[i] += buf[i];
      sample[i + off] = sum[i];
    }
  }

}
