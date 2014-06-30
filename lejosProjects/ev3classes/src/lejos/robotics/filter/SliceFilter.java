package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * Saimple filter to take a slice of another filter
 * 
 * @author Lawrie Griffiths
 *
 */
public class SliceFilter extends AbstractFilter {
	private int firstIndex, lastIndex;

	public SliceFilter(SampleProvider source, int firstIndex, int lastIndex) {
		super(source);
		this.firstIndex = firstIndex;
		this.lastIndex = lastIndex;
	}
	
	@Override
	public int sampleSize() {
		return (lastIndex+1-firstIndex);
	}
	
	@Override
	public void fetchSample(float sample[], int offset) {
		float[] buffer = new float[sampleSize];
		super.fetchSample(buffer, 0);
		for(int i=0;i<sampleSize();i++) {
			sample[offset+i] = buffer[firstIndex+i];
		}
	}
}
