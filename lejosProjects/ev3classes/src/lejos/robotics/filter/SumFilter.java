package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * This filter returns the sum of the N most recent samples.<br>
 * The number of samples used is specified in the constructor of the filter.<b>
 * 
 * @author Aswin
 * 
 */

public class SumFilter extends AbstractFilter {
  private SampleBuffer worker;
  
  public SumFilter(SampleProvider source, int length) {
    super(source);
    // The SumFilter class uses one of two inner classes, depending on the
    // length of the buffer
    if (length > 8) {
      worker = new SmartSum(source, length);
    }
    else {
      worker = new PlainSum(source, length);
    }
  }

  public void fetchSample(float[] sample, int offset) {
    worker.fetchSample(sample, offset);
  }
  
  protected int getActualSize() {
    return worker.getActualSize();
  }
  
  

  /**
   * Calculates the sum by adding all the elements in the array every time
   * 
   * @author Aswin
   * 
   */
  private class PlainSum extends SampleBuffer {
    float[] latest;

    private PlainSum(SampleProvider source, int length) {
      super(source, length);
      latest = new float[sampleSize];
    }

    public void fetchSample(float[] sample, int off) {
      super.fetchSample(latest, 0);

      for (int i = 0; i < sampleSize; i++) {
        float s = 0;
        for (int j = 0; j < actualSize; j++) {
          s += sampleBuffer[i + j * sampleSize];
        }
        sample[i] = s;
      }
    }
  }

  /**
   * Calculates the sum by maintaining a sum by substracting old samples and
   * adding new samples;
   * 
   * @author Aswin
   * 
   */
  private class SmartSum extends SampleBuffer {
    float[] sum;
    float[] oldest;
    float[] latest;
    int[]   notRecalculated;
    int     recalculateIn = 1024;

    private SmartSum(SampleProvider source, int length) {
      super(source, length);
      sum = new float[sampleSize];
      oldest = new float[sampleSize];
      latest = new float[sampleSize];
      notRecalculated = new int[sampleSize];
    }

    @Override
    public void fetchSample(float[] sample, int off) {
      /*
       * to increase performance this method keeps the sum in memory. If a new
       * sample is taken then the oldest available sample is substracted from
       * the sum and the new sample is added to it. This prevents the need of
       * going to the whole buffer every time. This method does not work well
       * with NaN values. Therefore the sum has to be recalculated from the
       * buffer when a NaN is dropped from it. There is also the risk of
       * building up small numerical errors. Therefor the sum is recalculated
       * every so often.
       */

      // get the oldest sample to substract from sum
      getOldest(oldest, 0);
      // get a fresh sample to add to the sum
      super.fetchSample(latest, 0);
      // update sum
      for (int i = 0; i < sampleSize; i++) {
        if (oldest[i] == Float.NaN || notRecalculated[i] > recalculateIn) {
          sum[i] = recalculateSum(i);
          notRecalculated[i] = 0;
        }
        else {
          sum[i] = sum[i] + latest[i] - oldest[i];
          notRecalculated[i]++;
        }
        sample[i + off] = sum[i];
      }
    }

    private float recalculateSum(int index) {
      float s = 0;
      for (int i = 0; i < actualSize; i++) {
        s += sampleBuffer[index + i * sampleSize];
      }
      return s;
    }

  }
}