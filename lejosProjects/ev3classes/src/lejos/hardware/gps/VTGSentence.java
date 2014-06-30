package lejos.hardware.gps;

import java.util.NoSuchElementException;

/**
 * VTGSentence is a Class designed to manage VTG Sentences from a NMEA GPS Receiver
 * 
 * $GPVTG
 * 
 * Track Made Good and Ground Speed.
 * 
 * eg1. $GPVTG,360.0,T,348.7,M,000.0,N,000.0,K*43
 * eg2. $GPVTG,054.7,T,034.4,M,005.5,N,010.2,K*41
 * 
 *            054.7,T      True course made good over ground, degrees
 *            034.4,M      Magnetic course made good over ground, degrees
 *            005.5,N      Ground speed, N=Knots
 *            010.2,K      Ground speed, K=Kilometers per hour
 * 
 * eg3. for NMEA 0183 version 3.00 active the Mode indicator field
 *      is added at the end
 *      $GPVTG,054.7,T,034.4,M,005.5,N,010.2,K,A*53
 *            A            Mode indicator (A=Autonomous, D=Differential,
 *                         E=Estimated, N=Data not valid)
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class VTGSentence extends NMEASentence{

	//RMC Sentence
	private String nmeaHeader = "";
	private final float KNOT = 1.852f;
	private float speed = 0f;
	private float trueCourse = 0f;
	private float magneticCourse = 0f;

	//Header
	public static final String HEADER = "$GPVTG";
	
	/**
	 * Returns the NMEA header for this sentence.
	 */
	@Override
	public String getHeader() {
		return HEADER;
	}
	
	/**
	 * Get true course, in degrees.
	 * 
	 * @return the true course in degrees 0.0 to 360.0
	 */
	public float getTrueCourse(){
		return trueCourse;
	}
	
	/**
	 * Get Speed in Kilometers
	 * 
	 * @return the speed
	 */
	public float getSpeed(){
		return speed;  
	}

	/**
	 * Parse a RMC Sentence
	 * 
	 * $GPVTG,054.7,T,034.4,M,005.5,N,010.2,K,A*53
	 */
	public void parse(String sentence) {
		
		String[] parts = sentence.split(",");

		try{
			
			//Processing VTG data
			
			nmeaHeader = parts[0];//$GPVTG
			
			if (parts[7].length() == 0){
				speed = 0;
			} else {
				speed = Float.parseFloat(parts[7]);
			}
			
			//System.out.println(speed);
			
		} catch(NoSuchElementException e) {
			//System.err.println("VTGSentence: NoSuchElementException");
		} catch(NumberFormatException e) {
			//System.err.println("VTGSentence: NumberFormatException");
		} catch(Exception e){
			//System.err.println("VTGSentence: Exception");
		}		
	}//End Parse
}//End Class
