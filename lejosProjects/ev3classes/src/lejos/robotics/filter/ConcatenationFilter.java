package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/**
 * Simple filter to concatenate two sources
 * 
 * @author Lawrie Griffiths
 */
public class ConcatenationFilter implements SampleProvider {
	private SampleProvider source1, source2;

	public ConcatenationFilter(SampleProvider source1, SampleProvider source2) {
		this.source1 = source1;
		this.source2 = source2;
	}

	@Override
	public int sampleSize() {
		return source1.sampleSize() + source2.sampleSize();
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		source1.fetchSample(sample,offset);
		source2.fetchSample(sample,source1.sampleSize() + offset);
	}
}
