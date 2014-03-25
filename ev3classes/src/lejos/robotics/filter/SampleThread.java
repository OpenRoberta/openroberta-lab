package lejos.robotics.filter;

import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class SampleThread extends AbstractFilter {
  
  float[] buffer;
  boolean running = true;
  private float sampleRate;
  int interval;
  boolean newSampleAvailable=false;

  /**
   * Create an instance and run at <code>sampleRate</code>.
   * 
   * @param source
   * A SampleProvider
   * @param sampleRate
   * The sample rate expressed in Hertz (Samples / second)
   */
  public SampleThread(SampleProvider source, float sampleRate) {
    super(source);
    setSampleRate(sampleRate);
    buffer=new float[sampleSize];
    Runner runner = new Runner();
    runner.setDaemon(true);
    runner.start();
  }
  

  public boolean isNewSampleAvailable() {
    return newSampleAvailable;
  }

  public synchronized void fetchSample(float[] dst, int off) {
    for (int axis=0;axis<sampleSize;axis++) 
      dst[axis+off]=buffer[axis];
    newSampleAvailable=false;
  }
  
  
  /**
   * Separate thread to continuously update the buffer with most recent sensor
   * data at fixed interval.
   * 
   * @author Aswin Bouwmeester
   * 
   */
  private class Runner extends Thread {
    @Override
    public void run() {
      long nextTime=System.currentTimeMillis();
      long currentTime;
      while (true) {
        nextTime += interval;
        if (running) {
          source.fetchSample(buffer,0);
          newSampleAvailable=true;
        }
        currentTime=System.currentTimeMillis();
        if (currentTime<nextTime)
          Delay.msDelay(nextTime-currentTime);
      }
    }

  }

  /**
   * @return rate in Hz
   */
  public float getSampleRate() {
    return sampleRate;
  }

  /**
   * @param rate in Hz
   */
  public void setSampleRate(float rate) {
    sampleRate=rate;
    interval=(int) (1000/sampleRate);
  }


  /**
   * Start the sampling (Default at instantiation)
   */
  public void start() {
    running=true;
  }

  /**
   * Stop the sampling
   */
  public void stop() {
    running=false;
  }


}
