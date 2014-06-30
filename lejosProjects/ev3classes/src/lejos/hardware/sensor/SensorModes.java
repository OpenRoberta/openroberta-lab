package lejos.hardware.sensor;

import java.util.ArrayList;

/**
 * Provide access to the modes supported by a sensor
 * @author andy
 *
 */
public interface SensorModes
{
    /**
     * Return a list of string descriptions for the sensors available modes.
     * @return list of string descriptions
     */
    public ArrayList<String> getAvailableModes();
    
    /**
     * Return the sample provider interface for the requested mode
     * @param mode the mode number
     * @return the sample provider for this mode
     */
    public SensorMode getMode(int mode);

    /**
     * Return the sample provider for the request mode
     * @param modeName the name/description of the mode
     * @return the sample provider for the requested mode.
     */
    public SensorMode getMode(String modeName);
    

}
