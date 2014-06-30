package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * This filter returns the maximum values found in the N most recent samples. <br>
 * The number of samples used is specified in the constructor of the filter.<b>
 * 
 * @author Aswin
 * 
 */
public class MaximumFilter extends SampleBuffer {

  float[] max;
  float[] buf, oldest;

  @Override
  public void fetchSample(float[] sample, int off) {
    getOldest(oldest, 0);
    super.fetchSample(buf, 0);
    for (int i = 0; i < sampleSize; i++) {
      // if the dropped sample happens to be the biggest sample, then rescan the
      // buffer for a smallest value;
      if (oldest[i] == max[i] || buf[i] > max[i]) {
        max[i] = Float.NEGATIVE_INFINITY;
        for (int j = 0; j < actualSize; j++) {
          max[i] = Math.max(sampleBuffer[j * sampleSize + i], max[i]);
        }
        sample[i + off] = max[i];
      }
    }
  }

  public MaximumFilter(SampleProvider source, int bufferSize) {
    super(source, bufferSize);
    max = new float[sampleSize];
    for (int i = 0; i < sampleSize; i++)
      max[i] = Float.NEGATIVE_INFINITY;
    buf = new float[sampleSize];
    oldest = new float[sampleSize];

  }

}
