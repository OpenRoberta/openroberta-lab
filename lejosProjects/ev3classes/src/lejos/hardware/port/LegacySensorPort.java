package lejos.hardware.port;

import lejos.hardware.port.AnalogPort;

/**
 * Abstraction for a port that supports legacy RCX sensors.
 * 
 * @author Lawrie Griffiths.
 *
 */
public interface LegacySensorPort extends AnalogPort {
	public void activate();	
	public void passivate();
}
