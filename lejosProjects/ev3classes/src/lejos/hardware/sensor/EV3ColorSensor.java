package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;
import lejos.robotics.Color;
import lejos.robotics.ColorIdentifier;
import lejos.robotics.LampController;

/**
 * Basic sensor driver for the Lego EV3 color sensor <BR>
 * TODO: Add extra methods to make it more compatible with the NXT, check SWITCH_DELAY
 * TODO: RGB mode seems to cause the sensor to crash!
 * @author andy
 *
 */
public class EV3ColorSensor extends UARTSensor implements LampController, ColorIdentifier, SensorMode
{
    protected static int[] colorMap =
    {
        Color.NONE, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.WHITE, Color.BROWN
    };
    protected static final int SWITCH_DELAY = 250;

    protected static final int COL_RESET = -1;
    protected static final int COL_REFLECT = 0;
    protected static final int COL_AMBIENT = 1;
    protected static final int COL_COLOR = 2;
    protected static final int COL_REFRAW = 3;
    protected static final int COL_RGBRAW = 4;
    protected static final int COL_CAL = 5;
    // following maps operating mode to lamp color
    protected static final int []lightColor = {Color.NONE, Color.RED, Color.BLUE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
    protected short[]raw = new short[3];
    
    private class ModeProvider implements SensorMode
    {
        final int mode;
        final int sampleSize;
        final String name;
        
        ModeProvider(String name, int mode, int sz)
        {
            this.name = name;
            this.mode = mode;
            sampleSize = sz;
        }

        @Override
        public int sampleSize()
        {
            return sampleSize;
        }

        @Override
        public void fetchSample(float[] sample, int offset)
        {
            switchMode(mode, SWITCH_DELAY);
            if (sampleSize == 1)
                sample[offset] = (float) (port.getByte() & 0xff)/100.0f;
            else
            {
                port.getShorts(raw, 0, raw.length);
                for(int i = 0; i < sampleSize; i++)
                    sample[offset+i] = (float)raw[i]/1020.0f;
            }
        }

        @Override
        public String getName()
        {
            return name;
        }
        
    }

    protected void initModes()
    {
        setModes(new SensorMode[]{getColorIDMode(), getRedMode(), getRGBMode(), getAmbientMode() });        
    }
    
    public EV3ColorSensor(UARTPort port)
    {
        super(port);
        initModes();
    }

    public EV3ColorSensor(Port port)
    {
        super(port);
        initModes();
    }


    /** {@inheritDoc}
     */    
    @Override
    public int getColorID()
    {
        setFloodlight(Color.WHITE);
        return colorMap[port.getByte()];
    }

    /** {@inheritDoc}
     */    
    @Override
    public void setFloodlight(boolean floodlight)
    {
        setFloodlight(floodlight ? Color.RED : Color.NONE);
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean isFloodlightOn()
    {
        return lightColor[currentMode+1] != Color.NONE;
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getFloodlight()
    {
         return lightColor[currentMode+1];
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean setFloodlight(int color)
    {
        int mode;
        switch (color)
        {
        case Color.BLUE:
            mode = COL_AMBIENT;
            break;
        case Color.WHITE:
            mode = COL_COLOR;
            break;
        case Color.RED:
            mode = COL_REFLECT;
            break;
        case Color.NONE:
            mode = COL_RESET;
            break;
        default:
            // TODO: Should we ignore a wrong color or throw an exception?
            throw new IllegalArgumentException("Invalid color specified");
        }
        switchMode(mode, SWITCH_DELAY);
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * get a sample provider in color ID mode
     * @return the sample provider
     */
    public SensorMode getColorIDMode()
    {
        return this;
    }
    
    /**
     * get a sample provider the returns the light value when illuminated with a
     * Red light source.
     * @return the sample provider
     */
    public SensorMode getRedMode()
    {
        return new ModeProvider("Red", COL_REFLECT, 1);
    }


    /**
     * get a sample provider the returns the light value when illuminated without a
     * light source.
     * @return the sample provider
     */
    public SensorMode getAmbientMode()
    {
        return new ModeProvider("Ambient", COL_AMBIENT, 1);
    }
    
    /**
     * get a sample provider that returns the light values (RGB) when illuminated by a
     * white light source.
     * @return the sample provider
     */
    public SensorMode getRGBMode()
    {
        //TODO: Should this return 3 or 4 values, 4 values would require an extra
        // mode switch to get ambient value.
        //TODO: This mode seems to crash the sensor. So return values have not been
        // verified
        return new ModeProvider("RGB", COL_RGBRAW, 3);
    }
    

    /** {@inheritDoc}
     */    
    @Override
    public int sampleSize()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    /** {@inheritDoc}
     */    
    @Override
    public void fetchSample(float[] sample, int offset)
    {
        sample[offset] = getColorID();        
    }

    @Override
    public String getName()
    {
        return "ColorID";
    }

}
