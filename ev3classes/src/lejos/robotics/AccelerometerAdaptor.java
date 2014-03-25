package lejos.robotics;

import lejos.robotics.filter.AbstractFilter;

public class AccelerometerAdaptor extends AbstractFilter implements Accelerometer{
  float[] sample;
  

  public AccelerometerAdaptor(SampleProvider source) {
    super(source);
    sample=new float[sampleSize];
  }
  
  protected int getElement(int index) {
    fetchSample(sample,0);
    return (int) sample[index];
  }

  @Override
  public int getXAccel() {
    return getElement(0);
  }

  @Override
  public int getYAccel() {
    return getElement(1);
  }

  @Override
  public int getZAccel() {
    return getElement(2);
  }

}
