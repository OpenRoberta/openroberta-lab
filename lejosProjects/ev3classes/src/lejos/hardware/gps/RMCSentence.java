package lejos.hardware.gps;

import java.util.NoSuchElementException;

/**
 * RMC is a Class designed to manage RMC Sentences from a NMEA GPS Receiver
 * 
 * RMC - NMEA has its own version of essential gps pvt (position, velocity, time) data. It is called RMC, The Recommended Minimum, which will look similar to:
 * 
 * $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
 * 
 * Where:
 *      RMC          Recommended Minimum sentence C
 *      123519       Fix taken at 12:35:19 UTC
 *      A            Status A=active or V=Void.
 *      4807.038,N   Latitude 48 deg 07.038' N
 *      01131.000,E  Longitude 11 deg 31.000' E
 *      022.4        Speed over the ground in knots
 *      084.4        Track angle in degrees True
 *      230394       Date - 23rd of March 1994
 *      003.1,W      Magnetic Variation
 *      *6A          The checksum data, always begins with *
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class RMCSentence extends NMEASentence{

	//RMC Sentence
	private String nmeaHeader = "";
	private int dateTimeOfFix = 0;
	private final int DATETIMELENGTH = 6;
	private String status = "";
	private final String ACTIVE = "A";
	private final String VOID = "V";
	private float latitude = 0;
	private String latitudeDirection = "";
	private float longitude = 0;
	private String longitudeDirection = "";
	private final float KNOT = 1.852f;
	private float groundSpeed;//In knots
	private int compassDegrees;
	private int dateOfFix = 0;
	private float magneticVariation = 0f;
	private String magneticVariationLetter = "";

	private float speed;//In Kilometers per hour

	//Header
	public static final String HEADER = "$GPRMC";

	/*
	 * GETTERS & SETTERS
	 */
	
	/**
	 * Returns the NMEA header for this sentence.
	 */
	@Override
	public String getHeader() {
		return HEADER;
	}
	
	public String getStatus(){
		return status;
	}
	
	/**
	 * Get Latitude
	 * 
	 */
	public float getLatitude(){
		return latitude;  
	}

	/**
	 * Get Longitude
	 * 
	 * @return the longitude
	 */
	public float getLongitude(){
		return longitude;
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
	 * Get time in integer format
	 * 
	 * @return the time
	 */
	public int getTime(){
		return dateTimeOfFix;
	}
	
	/**
	 * Get date in integer format
	 * 
	 * @return the date
	 */
	public int getDate(){
		return dateOfFix;
	}

	/**
	 * Return compass value from GPS
	 * 
	 * @return the compass heading
	 */
	public int getCompassDegrees(){
		return compassDegrees;
	}
	
	/**
	 * Parse a RMC Sentence
	 * 
	 * $GPRMC,081836,A,3751.65,S,14507.36,E,000.0,360.0,130998,011.3,E*62
	 */
	public void parse (String sentence) {
		
		String[] parts = sentence.split(",");
		
		try{		
			//Processing RMC data
			
			nmeaHeader = parts[0];//$GPRMC
		
			if (parts[1].length() == 0) {
				dateTimeOfFix = 0;
			} else {
				dateTimeOfFix = Math.round(Float.parseFloat(parts[1]));
			}
			
			if (parts[2].equals(ACTIVE)) {
				status = ACTIVE;
			} else {
				status = VOID;
			}
			
			if (isNumeric(parts[3])) {
				latitude = degreesMinToDegrees(parts[3],NMEASentence.LATITUDE);
			} else {
				latitude = 0f;
			}
			
			latitudeDirection = parts[4];
			
			if (isNumeric(parts[5])) {
				longitude = degreesMinToDegrees(parts[5],NMEASentence.LONGITUDE);
			} else {
				longitude = 0f;
			}

			longitudeDirection = parts[6];
			
			if (longitudeDirection.equals("E") == false) {
				longitude = -longitude;
			}
			
			if (latitudeDirection.equals("N") == false) {
				latitude = -latitude;
			}
			
			if (parts[7].length() == 0) {
				groundSpeed = 0f;
				speed = 0f;
			} else {
				groundSpeed = Float.parseFloat(parts[7]);
				
				//Speed
				if (groundSpeed > 0) {
					// km/h = knots * 1.852
					speed = groundSpeed * KNOT;
				}
				
				// A negative speed doesn't make sense.
				if (speed < 0) {
					speed = 0f;
				}
			}
			
			if (parts[8].length() == 0) {
				compassDegrees = 0;
			} else {
				compassDegrees = Math.round(Float.parseFloat(parts[8]));
			}
			
			if (parts[9].length() == 0) {
				dateOfFix = 0;
			} else{
				dateOfFix = Math.round(Float.parseFloat(parts[9]));
			}

			if (parts[10].length() == 0) {
				magneticVariation = 0;
			} else{
				magneticVariation = Math.round(Float.parseFloat(parts[10]));
			}
			
			if (parts[11].length() == 0) {
				magneticVariationLetter = "";
			} else{
				magneticVariationLetter = parts[11];
			}
		} catch(NoSuchElementException e) {
			//System.err.println("RMCSentence: NoSuchElementException");
		} catch(NumberFormatException e) {
		 	//System.err.println("RMCSentence: NumberFormatException");
		} catch(Exception e) {
			//System.err.println("RMCSentence: Exception");
		}
	}//End Parse
}//End Class
