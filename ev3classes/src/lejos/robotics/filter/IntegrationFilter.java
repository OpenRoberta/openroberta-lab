package lejos.robotics.filter;

import lejos.robotics.SampleProvider;


/**
 * Integrates samples over time. <br>
 * Usefull for example to:
 * <li>convert gyro output (degrees/second) to azymuth (Degrees)</li>
 * <li>Acceleration to speed</li>
 * <li>Speed to position</li>
 * @author Aswin
 *
 */
public class IntegrationFilter extends AbstractFilter{
	private long lastTime=0;
	private final float[]	currentValue;
	private static final float NANO=(float) Math.pow(10,-9);
	
	
	public IntegrationFilter(SampleProvider source) {
		super(source);
		currentValue=new float[sampleSize];
	}
	
	
	/**
	 * Sets the current value of the integrator to the specified value.
	 * @param value
	 * The value 
	 */
	public void resetTo(float value) {
		for (int i=0;i<sampleSize;i++)
			currentValue[i]=value;
		lastTime=0;
	}



	/**
	 * Fetches a sample from the source and then integrates it.
	 * 
	 * @see lejos.robotics.filter.AbstractFilter#fetchSample(float[], int)
	 */
	public void fetchSample(float sample[], int off) {
		super.fetchSample(sample, off);
		long now = System.nanoTime();
		if (lastTime == 0)
			lastTime = now;
		double dt = (now - lastTime) * NANO;
		lastTime = now;
		for (int i = 0; i < sampleSize; i++) {
			currentValue[i] += sample[i] * dt;
			sample[i + off] = currentValue[i];
		}
	}
}
