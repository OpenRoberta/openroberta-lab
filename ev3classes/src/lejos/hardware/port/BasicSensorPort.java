package lejos.hardware.port;

import lejos.hardware.sensor.SensorConstants;

/**
 * An abstraction for a sensor port that supports 
 * setting and retrieving types and modes of sensors.
 * 
 * @author Lawrie Griffiths.
 *
 */
public interface BasicSensorPort extends SensorConstants {

    public int getMode();
	
    @Deprecated
    public int getType();
	
    public boolean setMode(int mode);
	
    @Deprecated
    public boolean setType(int type);
	
    @Deprecated
    public boolean setTypeAndMode(int type, int mode);

}

