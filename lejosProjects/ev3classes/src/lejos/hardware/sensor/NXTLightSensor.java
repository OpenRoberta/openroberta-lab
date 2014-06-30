package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.robotics.Color;
import lejos.robotics.LampController;

/**
 * This class is used to obtain readings from a LEGO NXT light sensor.
 * The light sensor can be calibrated to low and high values. 
 * 
 */
public class NXTLightSensor extends AnalogSensor implements LampController, SensorConstants, SensorMode
{
    protected static final long SWITCH_DELAY = 10;
	private boolean floodlight = false;

	private class AmbientMode implements SensorMode
	{

        @Override
        public int sampleSize()
        {
            return 1;
        }

        @Override
        public void fetchSample(float[] sample, int offset)
        {
            setFloodlight(false);
            sample[offset] = 1.0f - normalize(port.getPin1());
        }

        @Override
        public String getName()
        {
            return "None";
        }
	    
	}
    protected void init()
    {
        setModes(new SensorMode[]{ this, new AmbientMode() });        
        setFloodlight(true);
    }
    
    /**
     * Create a light sensor object attached to the specified port.
     * The sensor will be set to floodlight mode, i.e. the LED will be turned on.
     * @param port port, e.g. Port.S1
     */
    public NXTLightSensor(AnalogPort port)
    {
        super(port);
        init();
    }
    
    /**
     * Create a light sensor object attached to the specified port.
     * The sensor will be set to floodlight mode, i.e. the LED will be turned on.
     * @param port port, e.g. Port.S1
     */
    public NXTLightSensor(Port port)
    {
        super(port);
        init();
    }
    
	public void setFloodlight(boolean floodlight)
	{
	        switchType(floodlight ? TYPE_LIGHT_ACTIVE : TYPE_LIGHT_INACTIVE, SWITCH_DELAY);
	        this.floodlight = floodlight;
	}
	
	public boolean setFloodlight(int color) {
		if(color == Color.RED) {
		    setFloodlight(true);
			return true;
		} else if (color == Color.NONE) {
            setFloodlight(false);
			return true;
		} else return false;
	}

	public int getFloodlight() {
		if(this.floodlight == true)
			return Color.RED;
		else
			return Color.NONE;
	}

	public boolean isFloodlightOn() {
		return this.floodlight;
	}

	public SensorMode getRedMode()
	{
	    return this;
	}
	
	public SensorMode getAmbientMode()
	{
	    return getMode(1);
	}
	
    @Override
    public int sampleSize()
    {
        return 1;
    }

    @Override
    public void fetchSample(float[] sample, int offset)
    {
        setFloodlight(true);
        sample[offset] = 1.0f - normalize(port.getPin1());
        
    }

    @Override
    public String getName()
    {
        return "Red";
    }
}
