package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * Provides a low-pass filter for samples <br>
 * 
 * @see <a
 *      href=http://en.wikipedia.org/wiki/Low-pass_filter>http://en.wikipedia.
 *      org/wiki/Low-pass_filter</a>
 * @author Aswin
 * 
 */
public class LowPassFilter extends AbstractFilter {
  float[] smoothed;
  long    lastTime = 0;
  float   timeConstant;

  /**
   * Constructor
   * 
   * @param source
   *          The source for getting samples
   * @param timeConstant
   *          The cut-off frequency for the filter
   */
  public LowPassFilter(SampleProvider source, float timeConstant) {
    super(source);
    smoothed = new float[sampleSize];
    this.timeConstant = timeConstant;
  }

  /**
   * Fetches a sample from the source and low-passes it
   * 
   * See http://en.wikipedia.org/wiki/Low-pass_filter
   */
  public void fetchSample(float[] dst, int off) {
    super.fetchSample(dst, off);
    if (lastTime == 0 || timeConstant == 0) {
      for (int axis = 0; axis < sampleSize; axis++) {
        smoothed[axis] = (dst[off + axis]);
      }
    }
    else {
      float dt = (float) ((System.currentTimeMillis() - lastTime) / 1000.0);
      float a = dt / (timeConstant + dt);
      for (int axis = 0; axis < sampleSize; axis++) {
        smoothed[axis] = (1f - a) * smoothed[axis] + a * (dst[off + axis]);
        dst[axis + off] = smoothed[axis];
      }
    }
    lastTime = System.currentTimeMillis();
  }

  public void setTimeConstant(float timeConstant) {
    this.timeConstant = timeConstant;
  }

}
