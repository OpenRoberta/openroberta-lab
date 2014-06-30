package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Supports HiTechnics EOPD (Electro Optical Proximity Detector) sensor.<br>
 * This sensor is used to detect objects and small changes in distance to a target.
 *  Unlike the LEGO light sensor it is not affected by other light sources.
 *  
 *  <A href="http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NEO1048">Description of the sensor</A>. <br>
 *  
 * 
 * @author Michael Smith <mdsmitty@gmail.com>
 * 
 */
public class HiTechnicEOPD extends AnalogSensor implements SensorConstants, SensorMode {



    protected static final long SWITCH_DELAY = 10;
    /**
     * @param port NXT sensor port 1-4
     */
    public HiTechnicEOPD (AnalogPort port){
        super(port);
        init();
    }
    
    /**
     * @param port NXT sensor port 1-4
     */
    public HiTechnicEOPD (Port port){
        super(port);
        init();
    }
		
    protected void init() {
    	setModes(new SensorMode[]{ this, new ShortDistanceMode() });
    }
    
	
	/**
	 * Return a sample provider for long range distance value. <br>
	 * The output of the sensor is corrected using square root and normilized to a range of 0 to 1.
	 * 
	 */
	public SensorMode getLongDistanceMode() {
		return this;
	}

  /**
   * Return a sample provider for long range distance value. <br>
   * This provider is used for measuring distance for white objects at short range. <br>
   * The output of the sensor is corrected using square root and normilized to a range of 0 to 1.
   */
	 public SensorMode getShortDistanceMode() {
	    return getMode(1);
	  }

	
	
	@Override
	public int sampleSize() {
		return 1;
	}
	
	@Override
	public void fetchSample(float[] sample, int offset) {
    switchType(TYPE_LIGHT_INACTIVE, SWITCH_DELAY);
    sample[offset] = (float) Math.sqrt((normalize(port.getPin1())));
	}

	@Override
	public String getName() {
		return "Long distance";
	}

  public class ShortDistanceMode implements SensorMode {

  @Override
  public int sampleSize() {
    return 1;
  }

  @Override
  public void fetchSample(float[] sample, int offset) {
    switchType(TYPE_LIGHT_ACTIVE, SWITCH_DELAY);
    sample[offset] = (float) Math.sqrt((normalize(port.getPin1())));
  }

  @Override
  public String getName() {
    return "Short distance";
  }

}
}
