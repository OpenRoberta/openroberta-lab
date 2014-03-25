package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.robotics.*;


/**
 *  This class is used to obtain readings from a legacy RCX light sensor, using an adapter cable
 *  to connect it to the NXT brick. The light sensor can be calibrated to low and high values.
 *  Note: The RCX light sensor is not very sensitive when the floodlight is turned off: 
 *  Dark is around 523, sunlight is around 634. When the floodlight is on, dark readings are around
 *  155 and sunlight is around 713.
 * 
 */
public class RCXLightSensor extends AnalogSensor implements SensorConstants, LampController, SensorMode {
	private static final int SWITCH_DELAY = 10;	
	private boolean floodlight = false;
	
	/**
	 * Create an RCX light sensor object attached to the specified port.
	 * The sensor will be activated, i.e. the LED will be turned on.
	 * @param port port, e.g. Port.S1
	 */
	public RCXLightSensor(Port port)
	{
		super(port);
		init();	
	}
	
    protected void init()
    {
        setModes(new SensorMode[]{ this, new AmbientMode() });
        port.setTypeAndMode(TYPE_REFLECTION, MODE_RAW);
        setFloodlight(true);
    }

	public int getFloodlight() {
		if(this.floodlight == true)
			return Color.RED;
		else
			return Color.NONE;
	}

	public boolean isFloodlightOn() {
		return floodlight;
	}

	public void setFloodlight(boolean floodlight) {
		this.floodlight = floodlight;
		if(floodlight == true)
			switchType(TYPE_REFLECTION, SWITCH_DELAY);
		else
	         switchType(TYPE_CUSTOM, SWITCH_DELAY);
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
	
	public SensorMode getRedMode()
	{
	    return this;
	}
	
	public SensorMode getAmbientMode()
	{
	    return getMode(1);
	}
	
	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
        setFloodlight(true);
        sample[offset] = 1.0f - normalize(port.getPin1());	
	}

	@Override
	public String getName() {
		return "Red";
	}
		
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
}
