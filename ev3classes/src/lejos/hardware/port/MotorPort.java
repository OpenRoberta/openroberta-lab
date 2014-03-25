package lejos.hardware.port;

import lejos.hardware.BrickFinder;

/**
 * 
 * Abstraction for an EV3 output port.
 * 
 * TODO: Sort out a better way to do this, or least clean up the magic numbers.
 *
 */
public interface MotorPort {
	
	/**
	 * MotorPort A.
	 */
	public static final Port A = BrickFinder.getDefault().getPort("A");
	
	/**
	 * MotorPort B.
	 */
	public static final Port B = BrickFinder.getDefault().getPort("B");
	
    /**
     * MotorPort C.
     */
    public static final Port C = BrickFinder.getDefault().getPort("C");
    
    /**
     * MotorPort D.
     */
    public static final Port D = BrickFinder.getDefault().getPort("D");

}
