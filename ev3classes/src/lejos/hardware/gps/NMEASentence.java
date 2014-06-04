package lejos.hardware.gps;

/**
 * Class designed to manage all NMEA Sentence.
 * 
 * GGA and RMC Sentence needs to validate data.
 * This class has methods to validate receivedad data
 * 
 * @author Juan Antonio Brenha Moral
 * @author BB
 *
 */
abstract public class NMEASentence {

	public static final int LATITUDE = 0;
	public static final int LONGITUDE = 1;

	/**
	 *  Retrieve the header constant for this sentence.
	 *  @return The NMEA header string ($GPGGA, $GPVTG, etc...)   
	 */
	// TODO: Maybe getSentenceType()?
	// TODO: Should it return the $ too, or maybe have a list of constants?
	abstract public String getHeader();
	
	/**
	 * Abstract method to parse out all relevant data from the nmeaSentence.
	 */
	abstract protected void parse(String sentence);
	
	/**
	 * Any GPS Receiver gives Lat/Lon data in the following way:
	 * 
	 * http://www.gpsinformation.org/dale/nmea.htm
	 * http://www.teletype.com/pages/support/Documentation/RMC_log_info.htm
	 * 
	 * 4807.038,N   Latitude 48 deg 07.038' N
	 * 01131.000,E  Longitude 11 deg 31.000' E
	 * 
	 * This data is necessary to convert to Decimal Degrees.
	 * 
	 * Latitude values has the range: -90 <-> 90
	 * Longitude values has the range: -180 <-> 180
	 * 
	 * @param DD_MM
	 * @param CoordinateType
	 * @return the degrees
	 */
	protected float degreesMinToDegrees(String DD_MM,int CoordinateType) {//throws NumberFormatException
		// This methods accept all strings of the format
		// DDDMM.MMMM
		// DDDMM
		// MM.MMMM
		// MM
		
		// check first character, rest is checked by parseInt/parseFloat
		int len = DD_MM.length();
		if (len <= 0 || DD_MM.charAt(0) == '-')
			throw new NumberFormatException();
		
		int dotPosition = DD_MM.indexOf('.');
		if (dotPosition < 0)
			dotPosition = len;
		
		int degrees;
		float minutes;		
		if (dotPosition > 2)
		{
			degrees = Integer.parseInt(DD_MM.substring(0, dotPosition-2));
			// check first character of minutes since '-' is not allowed
			// rest is checked by parseFloat
			if (DD_MM.charAt(dotPosition-2) == '-')
				throw new NumberFormatException();
			minutes = Float.parseFloat(DD_MM.substring(dotPosition-2));
		}
		else
		{
			degrees = 0;
			minutes = Float.parseFloat(DD_MM);
		}
		
//		if(CoordenateType == NMEASentence.LATITUDE){
//			if((degrees >=0) && (degrees <=90)){
//				throw new NumberFormatException();
//			}
//		}else{
//			if((degrees >=0) && (degrees <=180)){
//				throw new NumberFormatException();
//			}
//		}
		
		return degrees + minutes * (float)(1.0 / 60.0);
	}
	
	//Idea
	//http://rosettacode.org/wiki/Determine_if_a_string_is_numeric#Java
	public final boolean isNumeric(final String s) {
		if (s == null || s.isEmpty()) return false;
		for (int x = 0; x < s.length(); x++) {
		    final char c = s.charAt(x);
		    if (x == 0 && (c == '-')) continue;  // negative
		    if ((c >= '0') && (c <= '9')) continue;  // 0 - 9
		    if (c == '.') continue; // float or double values
		    return false; // invalid
		}
		return true; // valid
	}

}
