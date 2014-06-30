package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * Simple filter that adjusts the sample to use a specified zero value
 * 
 * @author Lawrie Griffiths
 *
 */
public class ZeroFilter extends AbstractFilter {
	private float[] zeroValue;
	float[] sample;
	
	public ZeroFilter(SampleProvider source, float[] zeroValue) {
		super(source);
		this.zeroValue = zeroValue;
	}

	public void fetchSample(float sample[], int offset) {
		super.fetchSample(sample, offset);
		for(int i=0;i<sampleSize();i++) {
			sample[offset+i] -= zeroValue[i];
		}
	}
}
