package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * This filter returns the mean values found in the N most recent samples. <br>
 * The number of samples used is specified in the constructor of the filter.<b>
 * 
 * @author Aswin
 * 
 */
public class MeanFilter extends SumFilter {

  public MeanFilter(SampleProvider source, int length) {
    super(source, length);
  }

  @Override
  public void fetchSample(float[] sample, int off) {
    super.fetchSample(sample, off);
    for (int i = 0; i < sampleSize; i++)
      sample[i + off] /= actualSize;
  }

}
