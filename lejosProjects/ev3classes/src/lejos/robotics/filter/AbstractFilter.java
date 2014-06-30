package lejos.robotics.filter;

import lejos.robotics.SampleProvider;

/** Base class for Sample filters
 * @author Kirk, Aswin
 *
 */
public abstract class AbstractFilter implements SampleProvider {
	protected final SampleProvider source;
	protected final int sampleSize;
	
	public AbstractFilter(SampleProvider source) {
		this.source=source;
		this.sampleSize=source.sampleSize();
	}

	@Override
	public int sampleSize() {
		return sampleSize;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		source.fetchSample(sample, offset);
	}

}
