package lejos.hardware.sensor;

import lejos.robotics.SampleProvider;

public interface SensorMode extends SampleProvider
{
    /**
     * return a string description of this sensor mode
     * @return The description/name of this mode
     */
    public String getName();
    
    // TODO: Return additional mode information

}
