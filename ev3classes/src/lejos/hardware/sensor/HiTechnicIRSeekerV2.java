package lejos.hardware.sensor; 

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;

/**
 * HiTechnic IRSeeker v2
 *  
 * See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NSK1042
 *
 * @author Lawrie Griffiths
 * 
 */
public class HiTechnicIRSeekerV2 extends I2CSensor implements SensorMode 
{
    private static final byte   address   = 0x10;
    private byte[] buf = new byte[1]; 

    public HiTechnicIRSeekerV2(I2CPort port) { 
       super(port, address);
       init();
    } 

    public HiTechnicIRSeekerV2(Port port)  { 
       super(port, address);
       init();
    } 
    
    protected void init() {
    	setModes(new SensorMode[]{ this, new UnmodulatedMode() });
    }
	
	public SensorMode getModulatedMode() {
		return this;
	}
	
 	@Override
	public int sampleSize() {
		return 1;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		getData(0x49, buf, 1);
		float angle = Float.NaN;
		if (buf[0] > 0) {
			// Convert to angle with zero forward, anti-clockwise positive
			angle = -(buf[0] * 30 - 150);
		}
		sample[offset] = angle;
	}	
	
	@Override
	public String getName() {
		return "Modulated";
	}
	
	public SensorMode getUnmodulatedMode() {
		return getMode(1);
	}
	
	private class UnmodulatedMode implements SensorMode {
		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			getData(0x42, buf, 1);
			float angle = Float.NaN;
			if (buf[0] > 0) {
				// Convert to angle with zero forward, anti-clockwise positive
				angle = -(buf[0] * 30 - 150);
			}
			sample[offset] = angle;	
		}

		@Override
		public String getName() {
			return "Unmodulated";
		}		
	}
}
