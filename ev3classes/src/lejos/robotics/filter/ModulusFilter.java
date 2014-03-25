package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * Simple filter that adjusts the sample to use a specified zero value mod a given value
 * 
 * @author Lawrie Griffiths
 *
 */
public class ModulusFilter extends AbstractFilter {
	private float[] zeroValue;
	float[] sample;
	float modulus;
	
	public ModulusFilter(SampleProvider source, float[] zeroValue, float modulus) {
		super(source);
		this.zeroValue = zeroValue;
		this.modulus = modulus;
	}

	public void fetchSample(float sample[], int offset) {
		super.fetchSample(sample, offset);
		for(int i=0;i<sampleSize();i++) {
			sample[offset+i] = (modulus + sample[offset+i] - zeroValue[i]) % modulus;
		}
	}
}
