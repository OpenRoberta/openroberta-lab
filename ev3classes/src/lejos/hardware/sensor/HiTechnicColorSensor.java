package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.robotics.Color;
import lejos.robotics.ColorIdentifier;

/**
 * HiTechnic color sensor.<br>
 * www.hitechnic.com
 * 
 * This class does support HiTechnic Color Sensor V2.
 * 
 * See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NCO1038
 *
 *@author BB extended by A.T.Brask, converted for EV3 by Lawrie Griffiths
 *
 */
public class HiTechnicColorSensor extends I2CSensor implements SensorMode, ColorIdentifier {
	
    private byte[] buf = new byte[3];

    // TODO: Problem: The following table ignores pastels and other subtle colors HiTechnic can detect.
    // Converting to limited JSE color set means this generic interface isn't as rich at describing color
    // ID as it could be. 
    private int [] colorMap = {Color.BLACK, Color.MAGENTA, Color.MAGENTA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.YELLOW,
    		Color.ORANGE, Color.RED, Color.RED, Color.MAGENTA, Color.MAGENTA, Color.YELLOW, Color.PINK,
    		Color.PINK, Color.PINK, Color.MAGENTA, Color.WHITE};
    
    public HiTechnicColorSensor(I2CPort port)
    {
        super(port);
        init();
    }

    public HiTechnicColorSensor(Port port)
    {
        super(port);
        init();
    }
    
    protected void init()
    {
        setModes(new SensorMode[]{this, new RGBMode()});        
    }

    // INDEX VALUES

    /**
     * Returns the color index detected by the sensor. 
     * @return Color index.<br>
     * <li> 0 = red
     * <li> 1 = green
     * <li> 2 = blue
     * <li> 3 = yellow
     * <li> 4 = magenta
     * <li> 5 = orange
     * <li> 6 = white
     * <li> 7 = black
     * <li> 8 = pink
     * <li> 9 = gray
     * <li> 10 = light gray
     * <li> 11 = dark gray 
     * <li> 12 = cyan
     */
    @Override
    public int getColorID()
    {
        getData(0x42, buf, 1);
        int HT_val = (0xFF & buf[0]);
        return colorMap[HT_val];
    }
    
    public SensorMode getColorIDMode() {
    	return this;
    }
    
    public SensorMode getRGBMode() {
    	return getMode(1);
    }
    
    public class RGBMode implements SensorMode {

		@Override
		public int sampleSize() {
			return 3;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
	    	getData(0x43, buf, 3);
	        for(int i=0;i<3;i++) sample[offset+i] = ((float) (0xFF & buf[i])) /256f;		
		}

		@Override
		public String getName() {
			return "RGB";
		}
    }

	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		sample[offset] = (float) getColorID();	
	}

	@Override
	public String getName() {
		return "ColorID";
	}
}
