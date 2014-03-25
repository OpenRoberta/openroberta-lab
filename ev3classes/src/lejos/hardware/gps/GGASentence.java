package lejos.hardware.gps;

import java.util.NoSuchElementException;

/**
 * This class has been designed to manage a GGA Sentence
 * 
 * GGA - essential fix data which provide 3D location and accuracy data.
 * 
 * $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
 * 
 * Where:
 *      GGA          Global Positioning System Fix Data
 *      123519       Fix taken at 12:35:19 UTC
 *      4807.038,N   Latitude 48 deg 07.038' N
 *      01131.000,E  Longitude 11 deg 31.000' E
 *      1            Fix quality: 0 = invalid
 *                                1 = GPS fix (SPS)
 *                                2 = DGPS fix
 *                                3 = PPS fix
 * 			       4 = Real Time Kinematic
 * 			       5 = Float RTK
 *                                6 = estimated (dead reckoning) (2.3 feature)
 * 			       7 = Manual input mode
 * 			       8 = Simulation mode
 *      08           Number of satellites being tracked
 *      0.9          Horizontal dilution of position
 *      545.4,M      Altitude, Meters, above mean sea level
 *      46.9,M       Height of geoid (mean sea level) above WGS84
 *                       ellipsoid
 *      (empty field) time in seconds since last DGPS update
 *      (empty field) DGPS station ID number
 *      *47          the checksum data, always begins with *
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class GGASentence extends NMEASentence{
	
	//GGA
	private String nmeaHeader = "";
	private int dateTimeOfFix = 0;
	private float latitude = 0;
	private char latitudeDirection;
	private float longitude = 0;
	private char longitudeDirection;
	private int quality = 0;
	private int satellitesTracked = 0;
	private float hdop = 0;
	private float altitude = 0;
	private String altitudeUnits;
	private float geoidalSeparation;
	private String geoidalSeparationUnit;

	//Header
	public static final String HEADER = "$GPGGA";
	
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

	/**
	 * Get Latitude
	 * 
	 */
	public float getLatitude() {
		return latitude;
	}
	
	/**
	 * Get Latitude Direction
	 * 
	 * @return
	 */
	public char getLatitudeDirection(){
		return latitudeDirection;
	}
	
	/**
	 * Get Longitude
	 * 
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * Get Longitude Direction
	 * @return
	 */
	public char getLongitudeDirection(){
		return longitudeDirection;
	}
	
	/**
	 * Get Altitude
	 * 
	 * @return
	 */
	public float getAltitude(){
		return altitude;
	}

	/**
	 * Returns the last time stamp retrieved from a satellite
	 * 
	 * @return The time as a UTC integer. 123519 = 12:35:19 UTC
	 */
	public int getTime(){
		return dateTimeOfFix;
	}
	
	/**
	 * Returns the number of satellites being tracked to
	 * determine the coordinates.
	 * 
	 * @return Number of satellites e.g. 8
	 */
	public int getSatellitesTracked() {
		return satellitesTracked;
	}

	/**
	 * Get GPS Quality Data
	 * 
	 * @return the fix quality
	 */
	public int getFixQuality(){
		return quality;
	}

	
	/**
	 * Method used to parse a GGA Sentence
	 */
	protected void parse(String sentence) {
		
		String[] parts = sentence.split(",");

		try{
			//Processing GGA data
			
			nmeaHeader = parts[0];

			if (parts[1].length() == 0) {
				dateTimeOfFix = 0;
			} else {
				dateTimeOfFix = Math.round(Float.parseFloat(parts[1]));
			}
						
			if (isNumeric(parts[2])) {
				latitude = degreesMinToDegrees(parts[2],NMEASentence.LATITUDE);
			} else {
				latitude = 0f;
			}
			
			latitudeDirection = parts[3].charAt(0);
			
			if (isNumeric(parts[4])) {
				longitude = degreesMinToDegrees(parts[4],NMEASentence.LONGITUDE);
			} else {
				longitude = 0f;
			}

			longitudeDirection = parts[5].charAt(0);
			
			if (longitudeDirection != 'E') {
				longitude = -longitude;
			}
			
			if (latitudeDirection != 'N') {
				latitude = -latitude;
			}

			if (parts[6].length() == 0) {
				quality = 0;
			} else{
				quality = Math.round(Float.parseFloat(parts[6]));//Fix quality
			}

			if (parts[7].length() == 0) {
				satellitesTracked = 0;
			} else{
				satellitesTracked = Math.round(Float.parseFloat(parts[7]));
			}

			if (parts[8].length() == 0) {
				hdop = 0;
			} else {
				hdop = Float.parseFloat(parts[8]);//Horizontal dilution of position
			}
			
			if (isNumeric(parts[9])) {
				altitude = Float.parseFloat(parts[9]);
			} else{
				altitude = 0f;
			}	
		} catch(NoSuchElementException e) {
			//System.err.println("GGASentence: NoSuchElementException");
		} catch(NumberFormatException e) {
			//System.err.println("GGASentence: NumberFormatException");
		} catch(Exception e){
			//System.err.println("GGASentence: Exception");
		}
	}//End parse
}//End class
