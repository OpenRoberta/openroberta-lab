package lejos.utility;

/**
 * Integrate sensor measurement over time, e.g. to calculate velocity from acceleration
 * or angle from angular velocity.
 * 
 * @author Lawrie Griffiths
 *
 */
public class Integration {
	private long lastReadingMillis;
	private double lastReading;
	private double integral;
	
	/**
	 * Create the integration object with the initial value for the integral value
	 * and the initial reading.
	 * 
	 * @param initialValue the initial value for the integral
	 * @param reading the initial reading
	 */
	public Integration(double initialValue, double reading) {
		integral = initialValue;
		lastReadingMillis = System.currentTimeMillis();
		lastReading = reading;		
	}
	
	/**
	 * Add a new reading and return the current integral
	 * 
	 * @param reading the reading as a double
	 * @return the current value of the integral
	 */
	public double addReading(double reading) {
		// Assume linear change between measurements
		double averageReading = (reading + lastReading)/2;
		long millis = System.currentTimeMillis();
		double timeInterval = (double) (millis - lastReadingMillis) / 1000d;
		
		lastReadingMillis = millis;		
		integral += (averageReading * timeInterval);
		lastReading = reading;
		
		return integral;
	}
}
