package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * This filter returns the median value found in the N most recent samples. <br>
 * The number of samples used is specified in the constructor of the filter.<b>
 * 
 * @author Aswin
 * 
 */
public class MedianFilter extends SampleBuffer {

  public MedianFilter(SampleProvider source, int bufferSize) {
    super(source, bufferSize);
  }

  @Override
  public void fetchSample(float[] sample, int off) {
    float current, smallest, value;
    int n, halfWay;
    super.fetchSample(sample, off);
    for (int i = 0; i < sampleSize; i++) {
      current = Float.NEGATIVE_INFINITY;
      n = 0;
      halfWay = actualSize / 2;
      while (n <= halfWay) {
        smallest = Float.POSITIVE_INFINITY;
        for (int j = 0; j < actualSize; j++) {
          value = sampleBuffer[currentPos * sampleSize + i];
          if (value == smallest)
            n++;
          else if (value > current && value < smallest)
            smallest = value;
        }
        current = smallest;

        n++;
      }
      sample[i + off] = current;
    }

  }

}
