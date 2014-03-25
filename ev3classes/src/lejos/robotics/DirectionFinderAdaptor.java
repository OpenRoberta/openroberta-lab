package lejos.robotics;

import lejos.robotics.filter.ModulusFilter;

public class DirectionFinderAdaptor implements DirectionFinder {
	private Calibrate calibrator;
	private SampleProvider provider, initialProvider;
	private float[] sample = new float[1];
	
	public DirectionFinderAdaptor(SampleProvider provider) {
		this.provider = provider;
	    initialProvider = provider;
		if (provider instanceof Calibrate) this.calibrator = (Calibrate) provider;
	}
	
	@Override
	public float getDegreesCartesian() {
		provider.fetchSample(sample, 0);
		return sample[0];
	}

	@Override
	public void startCalibration() {
		if (calibrator != null) calibrator.startCalibration();
	}

	@Override
	public void stopCalibration() {	
		if (calibrator != null) calibrator.stopCalibration();
	}

	@Override
	public void resetCartesianZero() {
		float[] sample = new float[1];
		initialProvider.fetchSample(sample,0); 
		provider = new ModulusFilter(initialProvider, sample, 360);
	}

}
