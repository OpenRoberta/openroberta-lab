package lejos.hardware.sensor;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import lejos.utility.EndianTools;

/**
 * Supports the angle sensor of HiTechnic.
 * This Java implementation was based on the NXC implementation on http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NAA1030.
 * Works clockwise, i.e. rotating clockwise increases angle value and rotating counter-clockwise decreases angle value.
 *
 * @author Michael Mirwaldt (epcfreak@gmail.com)<br/>
 * date 2nd April 2011
 */

public class HiTechnicAngleSensor extends I2CSensor implements SensorMode {
   protected static final int REG_ANGLE = 0x42;
   protected static final int REG_ACCUMULATED_ANGLE = 0x44;
   protected static final int REG_SPEED = 0x48;
   protected static final int HTANGLE_MODE_CALIBRATE = 0x43;
   protected static final int HTANGLE_MODE_RESET = 0x52;
   
   private byte buf[] = new byte[4];

   public HiTechnicAngleSensor(I2CPort port) {
       super(port, DEFAULT_I2C_ADDRESS);
       init();
    }
    
   public HiTechnicAngleSensor(Port port) {
       super(port);
       init();
    }
   
   protected void init() {
   	setModes(new SensorMode[]{ this, new AccumulatedAngleMode(), new AngularVelocityMode() });
   }
   
   /** 
    * Reset the rotation count of accumulated angle to zero. 
    * Not saved in EEPORM.
    */
   public void resetAccumulatedAngle() {
      sendData(0x41, (byte) HTANGLE_MODE_RESET);
   }
   
   /** 
    * Calibrate the zero position of angle.
    * Zero position is saved in EEPROM on sensor.
    * Thread sleeps for 50ms while that is done.
    */
   public void calibrateAngle() {
      sendData(0x41, (byte) HTANGLE_MODE_CALIBRATE);
      Delay.msDelay(50);
   }
   
   public SensorMode getAngleMode() {
	   return this;
   }

	@Override
	public int sampleSize() {
		return 1;
	}
	
	@Override
	public void fetchSample(float[] sample, int offset) {
	    getData(REG_ANGLE, buf, 2);
	    int bits9to2 = buf[0] & 0xFF;
	    int bit1 = buf[1] & 0x01;
	    
	    sample[offset] = (bits9to2 << 1) | bit1;		
	}
	
	@Override
	public String getName() {
		return "Angle";
	}
	
	public SensorMode getAccumulatedAngleMode() {
		return getMode(1);
	}
	
	private class AccumulatedAngleMode implements SensorMode {
		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
	      getData(REG_ACCUMULATED_ANGLE, buf, 4); 
	      
	      sample[offset] = -EndianTools.decodeIntBE(buf, 0);
		}

		@Override
		public String getName() {
			return "AccumulatedAngle";
		}		
	}
	
	public SensorMode getAngularVelocityMode() {
		return getMode(2);
	}
	
	private class AngularVelocityMode implements SensorMode {
		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
	      getData(REG_SPEED, buf, 2);	      
		  sample[offset] = -EndianTools.decodeShortBE(buf, 0) / 60;
		}

		@Override
		public String getName() {
			return "AngularVelocity";
		}		
	}
}
