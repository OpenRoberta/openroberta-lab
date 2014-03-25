package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * Provides a buffer to store samples
 * 
 * @author Aswin
 * 
 */
public abstract class SampleBuffer extends AbstractFilter {

  int     bufferSize;
  int     actualSize = 0;
  int     currentPos = 0;
  float[] sampleBuffer;

  public SampleBuffer(SampleProvider source, int bufferSize) {
    super(source);
    if (bufferSize < 1)
      throw new IllegalArgumentException();
    this.bufferSize = bufferSize;
    reset();
  }

  public int getBufferSize() {
    return bufferSize;
  }

  /**
   * Empties the sample buffer
   */
  private void reset() {
    currentPos = 0;
    actualSize = 0;
    sampleBuffer = new float[bufferSize * sampleSize];
  }

  int toPos(int i, int index) {
    return index * sampleSize + i;
  }

  public void fetchSample(float[] sample, int off) {
    source.fetchSample(sample, off);
    for (int i = 0; i < sampleSize; i++) {
      sampleBuffer[currentPos * sampleSize + i] = sample[i + off];
    }

    if (actualSize < bufferSize)
      actualSize += 1;
    currentPos = (currentPos + 1) % bufferSize;
  }

  protected void getOldest(float[] sample, int off) {
    for (int i = 0; i < sampleSize; i++) {
      sample[i + off] = sampleBuffer[currentPos * sampleSize + i];
    }
  }

}
