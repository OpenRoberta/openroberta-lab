package lejos.robotics.filter;

import lejos.robotics.SampleProvider;


/**
 * The offsetCorrectionFilter calculates and applies an offset correction for samples. 
 * It does so by monitoring sample values and comparing these to a reference value. 
 * The difference between the sample and the reference is the offset error. 
 * The offset error is used to slowly update the offset correction term. 
 * The filter substracts the offset correction term from the sample before passing it on. <br>
 * <br>offsetError=reference - sample <br>
 * offsetCorrection=(1-updateSpeed)*offsetCorrection+updateSpeed*offsetError<br>
 * sample=sample-offsetCorrection
 *  
 * @author Aswin
 *
 */
public class OffsetCorrectionFilter extends AbstractFilter {
  static int SETTLE=10;
	float speed=0, endSpeed=0;
	float[] offset; 
	float[] reference;
	float error;
	int n=0;
	
	
	/** Constructor for the offset correction filter using default parameters. <br>
	 * All samples are corrected to a reference value of zero with an update speed of 0.1.
	 * @param source
	 * source for sample
	 */
	public OffsetCorrectionFilter(SampleProvider source) {
		this(source,new float[]{0,0,0});
	}
	
	/** Constructor for the offset correction filter using default update speed. <br>
	 * All samples are corrected to the specified reference value  with an update speed of 0.1.
	 * @param source
	 * source for sample
	 * @param reference
	 * An array with reference values. The array length should match the sample size.
	 */
	public OffsetCorrectionFilter(SampleProvider source, float[] reference) {
		this(source,reference,0.1f);
	}
	
	
	
	/** Constructor for the offset correction filter.<br>
	 * Constructor
	 * @param source
	 * Source for sample
	 * @param reference
	 * An array with reference values. The array length should match the sample size.
	 * @param speed
	 * Speed to update offset value
	 */
	public OffsetCorrectionFilter(SampleProvider source, float[] reference, float speed) {
		this(source,reference,speed,speed);
	}

	/**
	 * Constructor for the offset correction filter. <br>
	 * This constrctor takes two parameters for update speed. This is usefull for sensor that have a big offset error.
	 * By giving a high starting speed for offset correction the filter settles quickly to an appropriate offset correction.
	 * By giving a lower end speed the filter is less sensitive to disturbances. 
	 *  By
	 * @param source
	 * Source for sample
	 * @param reference
	 * An array with reference values. The array length should match the sample size.
	 * @param speed
	 * Begin speed to update offset value
	 * @param endSpeed
	 * End speed to update offset value
	 */
	public OffsetCorrectionFilter(SampleProvider source, float[] reference, float speed, float endSpeed) {
		super(source);
		offset=new float[sampleSize];
		this.speed=speed;
		this.reference=reference;
		this.endSpeed=endSpeed;
	}


	public void fetchSample(float[] dst, int off) {
		super.fetchSample(dst, off);

		for (int i=0;i<sampleSize;i++) {
		  if (n<SETTLE) {
		    offset[i]=((offset[i]*n+dst[i+off]))/(n+1);
		    dst[i+off]-=offset[i];
		  }
		  else {
  			error=dst[i+off]-reference[i];
  			offset[i]=offset[i]*(1.0f-speed)+error*speed;
  			dst[i+off]-=offset[i];
  		}
  		
  		if (endSpeed<speed) {
  			speed*=0.95f;
  			if (speed<endSpeed) {
  				speed=endSpeed;
  			}
  		}
		}
		n++;
	}
}
