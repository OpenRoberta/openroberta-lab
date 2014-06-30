package lejos.robotics;

/**
 * Interface for pressure sensors.
 * 
 * @author Lawrie Griffiths
 *
 */
public interface PressureDetector {
	
    /**
     * Get the pressure reading in kilopascals
     * 
     * @return the pressure in kPa
     */
	public float getPressure();
}
