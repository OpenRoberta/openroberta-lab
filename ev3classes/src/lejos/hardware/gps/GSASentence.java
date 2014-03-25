package lejos.hardware.gps;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * This class has been designed to manage a GSA Sentence
 * 
 * GPS DOP and active satellites
 * 
 * eg1. $GPGSA,A,3,,,,,,16,18,,22,24,,,3.6,2.1,2.2*3C
 * eg2. $GPGSA,A,3,19,28,14,18,27,22,31,39,,,,,1.7,1.0,1.3*35
 * 
 * 1    = Mode:
 *        M=Manual, forced to operate in 2D or 3D
 *        A=Automatic, 3D/2D
 * 2    = Mode:
 *        1=Fix not available
 *        2=2D
 *        3=3D
 * 3-14 = IDs of SVs used in position fix (null for unused fields)
 * 15   = PDOP
 * 16   = HDOP
 * 17   = VDOP
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */

public class GSASentence extends NMEASentence{
	//GSA
	private String nmeaHeader = "";
	private String mode = "";
	private int modeValue = 0;
	private final int MAXIMUMSV = 12;
	private int[] SV;
	private float PDOP = 0f;
	private float HDOP = 0f;
	private float VDOP = 0f;

	//Header
	public static final String HEADER = "$GPGSA";
	
	/**
	 * Constructor
	 */
	public GSASentence(){
		SV = new int[MAXIMUMSV];
	}
	
	/**
	 * Returns the NMEA header for this sentence.
	 */
	@Override
	public String getHeader() {
		return HEADER;
	}

	/**
	 * Return Mode1.
	 * Mode1 can receive the following values:
	 * 
	 * M=Manual, forced to operate in 2D or 3D
	 * A=Automatic, 3D/2D
	 * 
	 */
	public String getMode(){
		return mode;
	}

	/**
	 * Return Mode2.
	 * Mode1 can receive the following values:
	 * 
	 * 1=Fix not available
	 * 2=2D
	 * 3=3D
	 * 
	 */
	public int getModeValue(){
		return modeValue;
	}

	/**
	 * Return an Array with Satellite IDs
	 * 
	 * @return
	 */
	public int[] getPRN(){
		return SV;
	}

	/**
	 * Return PDOP
	 * 
	 * @return
	 */
	public float getPDOP(){
		return PDOP;
	}

	/**
	 * Return HDOP
	 * 
	 * @return
	 */
	public float getHDOP(){
		return HDOP;
	}

	/**
	 * Return VDOP
	 * 
	 * @return
	 */
	public float getVDOP(){
		return VDOP;
	}

	/**
	 * Method used to parse a GGA Sentence
	 */
	protected void parse(String sentence){
		
		//TODO StringTokenizer must not be used to parse NMEA sentences since it doesn't return empty tokens 
		StringTokenizer st = new StringTokenizer(sentence,",");

		try{
			
			//Extracting data from a GSA Sentence
			
			String part1 = st.nextToken();//NMEA header
			String part2 = st.nextToken();//mode
			String part3 = st.nextToken();//modeValue
			
			//Processing GSA data
			
			nmeaHeader = part1;
			mode = part2;
			
			if(part3 == null){
				modeValue = 0;
			}else{
				if(part3.length() == 0){
					modeValue = 0;
				}else{
					modeValue = Math.round(Float.parseFloat(part3));
				}
			}

			for(int i=0;i<12;i++){
				String part = st.nextToken();
				if(part.length() > 0){
					SV[i] = Integer.parseInt(part);
				}else{
					SV[i] = 0;
				}
			}
			
			String part16 = st.nextToken();//PDOP
			String part17 = st.nextToken();//HDOP
			String part18 = st.nextToken();//VDOP
			
			st = null;
			
			if(part16.length() == 0){
				PDOP = 0;
			}else{
				PDOP = Float.parseFloat(part16);
			}

			if(part17.length() == 0){
				HDOP = 0;
			}else{
				HDOP = Float.parseFloat(part17);
			}
			
			if(part18.length() == 0){
				VDOP = 0;
			}else{
				VDOP = Float.parseFloat(part18);
			}

		}catch(NoSuchElementException e){
			//System.err.println("GSASentence: NoSuchElementException");
		}catch(NumberFormatException e){
			//System.err.println("GSASentence: NumberFormatException");
		}catch(Exception e){
			//System.err.println("GSASentence: Exception");
		}

	}//End parse
}//End Class