package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;

/**
 * HiTechnic IRSeeker sensor - untested.
 * www.hitechnic.com
 * 
 */
public class HiTechnicIRSeeker extends I2CSensor implements SensorMode {
	byte[] buf = new byte[1];
	
    public HiTechnicIRSeeker(I2CPort port) {
        super(port);
        init();
    }
    
    public HiTechnicIRSeeker(Port port) {
        super(port);
        init();
    }
    
    protected void init() {
    	setModes(new SensorMode[]{ this });
    }
	
	public SensorMode getUnmodulatedMode() {
		return this;
	}

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
