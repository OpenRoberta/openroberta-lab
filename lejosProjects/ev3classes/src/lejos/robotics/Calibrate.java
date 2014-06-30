package lejos.robotics;

public interface Calibrate {
	
	/**
	 * Starts calibration.
	 * Must call stopCalibration() when done.
	 */
	public void startCalibration();
	
	/**
	 * Ends calibration sequence.
	 */
	public void stopCalibration();
}
