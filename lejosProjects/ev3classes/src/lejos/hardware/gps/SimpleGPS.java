package lejos.hardware.gps;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class manages data received from a GPS Device.
 * SimpleGPS Class manages the following NMEA Sentences
 * which supply location, heading, and speed data:
 * 
 * <li>GPGGA (location data)
 * <li>GPVTG (heading and speed data)
 * <li>GPGSA (accuracy information)
 * 
 * <p>This class is primarily for use by the javax.microedition.location package. The preferred
 * class to use for obtaining GPS data is the GPS class.</p>
 * 
 * @author BB
 * @author Juan Antonio Breña Moral
 * 
 */
public class SimpleGPS extends Thread {


	private InputStream in;

	//Classes which manages GGA, VTG, GSA Sentences
	protected GGASentence ggaSentence;
	protected VTGSentence vtgSentence;
	protected GSASentence gsaSentence;
	
	// Security
	private boolean close = false;
	
	// Listener-notifier
	static private ArrayList<GPSListener> listeners = new ArrayList<GPSListener>();

	
	/**
	 * The constructor. It needs an InputStream
	 * 
	 * @param in An input stream from the GPS receiver
	 */
	public SimpleGPS(InputStream in) {
		this.in = new BufferedInputStream(in, 128);
		
		ggaSentence = new GGASentence();
		vtgSentence = new VTGSentence();
		gsaSentence = new GSASentence();

		this.setDaemon(true); // Must be set before thread starts
		this.start();
	}


	/**
	 * Get Latitude
	 * 
	 * @return the latitude
	 */
	public double getLatitude() {
		return ggaSentence.getLatitude();
	}

	
	/**
	 * Get Latitude Direction
	 * 
	 * @return the latitude direction
	 */
	public char getLatitudeDirection(){
		return ggaSentence.getLatitudeDirection();
	}


	/**
	 * Get Longitude
	 * 
	 * @return the longitude
	 */
	public double getLongitude() {
		return ggaSentence.getLongitude();
	}

	/**
	 * Get Longitude Direction
	 * 
	 * @return the longitude direction
	 */
	public char getLongitudeDirection(){
		return ggaSentence.getLongitudeDirection();
	}

	
	/**
	 * The altitude above mean sea level
	 * 
	 * @return Meters above sea level e.g. 545.4
	 */
	public float getAltitude(){
		return ggaSentence.getAltitude();
	}

	/**
	 * Returns the number of satellites being tracked to
	 * determine the coordinates.
	 * @return Number of satellites e.g. 8
	 */
	public int getSatellitesTracked(){
		return ggaSentence.getSatellitesTracked();
	}

	/**
	 * Fix quality: 
	 * <li>0 = invalid
	 * <li>1 = GPS fix (SPS)
	 * <li>2 = DGPS fix
	 * <li>3 = PPS fix
	 * <li>4 = Real Time Kinematic
	 * <li>5 = Float RTK
	 * <li>6 = estimated (dead reckoning) (2.3 feature)
	 * <li>7 = Manual input mode
	 * <li>8 = Simulation mode
	 * 
	 * @return the fix quality
	 */
	public int getFixMode(){
		return ggaSentence.getFixQuality();
	}
	
	/**
	 * Get the last time stamp from the satellite for GGA sentence.
	 * 
	 * @return Time as a UTC integer. 123459 = 12:34:59 UTC
	 */
	public int getTimeStamp() { 
		return ggaSentence.getTime();
	}
	
	/**
	 * Get speed in kilometers per hour
	 * 
	 * @return the speed in kilometers per hour
	 */
	public float getSpeed() {
		return vtgSentence.getSpeed();
	}

	/**
	 * Get the course heading of the GPS unit.
	 * @return course (0.0 to 360.0)
	 */
	public float getCourse() {
		return vtgSentence.getTrueCourse();
	}
	
	/**
	 * Selection type of 2D or 3D fix 
	 * <li> 'M' = manual
	 * <li> 'A' = automatic 
	 * @return selection type - either 'A' or 'M'
	 */
	public String getSelectionType(){
		return gsaSentence.getMode();
	}

	/**
	 *  3D fix - values include:
	 *  <li>1 = no fix
	 *  <li>2 = 2D fix
	 *  <li>3 = 3D fix
	 * 
	 * @return fix type (1 to 3)
	 */
	public int getFixType(){
		return gsaSentence.getModeValue();
	}
	
	/**
	 * Get an Array of Pseudo-Random Noise codes (PRN). You can look up a list of GPS satellites by 
	 * this number at: http://en.wikipedia.org/wiki/List_of_GPS_satellite_launches
	 * Note: This number might be similar or identical to SVN. 
	 * 
	 * @return array of PRNs
	 */
	public int[] getPRN(){
		return gsaSentence.getPRN();
	}
	
	/**
	 * Get the 3D Position Dilution of Precision (PDOP). When visible GPS satellites are close
	 * together in the sky, the geometry is said to be weak and the DOP value is high; when far
	 * apart, the geometry is strong and the DOP value is low. Thus a low DOP value represents
	 * a better GPS positional accuracy due to the wider angular separation between the 
	 * satellites used to calculate a GPS unit's position. Other factors that can increase 
	 * the effective DOP are obstructions such as nearby mountains or buildings.
	 * 
	 * @return The PDOP (PDOP * 6 meters = the error to expect in meters) -1 means PDOP is unavailable from the GPS.
	 */
	public float getPDOP(){
		return gsaSentence.getPDOP();
	}

	/**
	 * Get the Horizontal Dilution of Precision (HDOP). When visible GPS satellites are close
	 * together in the sky, the geometry is said to be weak and the DOP value is high; when far
	 * apart, the geometry is strong and the DOP value is low. Thus a low DOP value represents
	 * a better GPS positional accuracy due to the wider angular separation between the 
	 * satellites used to calculate a GPS unit's position. Other factors that can increase 
	 * the effective DOP are obstructions such as nearby mountains or buildings.
	 * 
	 * @return the HDOP (HDOP * 6 meters = the error to expect in meters) -1 means HDOP is unavailable from the GPS.
	 */
	public float getHDOP(){
		return gsaSentence.getHDOP();
	}

	/**
	 * Get the Vertical Dilution of Precision (VDOP). When visible GPS satellites are close
	 * together in the sky, the geometry is said to be weak and the DOP value is high; when far
	 * apart, the geometry is strong and the DOP value is low. Thus a low DOP value represents
	 * a better GPS positional accuracy due to the wider angular separation between the 
	 * satellites used to calculate a GPS unit's position. Other factors that can increase 
	 * the effective DOP are obstructions such as nearby mountains or buildings.
	 * 
	 * @return the VDOP (VDOP * 6 meters = the error to expect in meters) -1 means VDOP is unavailable from the GPS.
	 */
	public float getVDOP(){
		return gsaSentence.getVDOP();
	}

	/**
	 * Method used to close connection. There is no real need to call this method.
	 * Included in case programmer wants absolutely clean exit. 
	 */
	public void close() throws IOException {
		this.close = true;
		in.close();
	}
	
	/*
	 * EVENTS SECTION
	 * 
	 */
	
	protected static synchronized void notifyListeners(NMEASentence sen){
		/* TODO: Problem is ggaSentence is a reused object in this API.
		 * Should really pass a copy of the NMEASentence to notify (and the copy
		 * must have all the appropriate GGA data, not just NMEA). However, check
		 *  if there are any listeners before making unnecessary copy. */
		
		for(int i=0; i<listeners.size();i++){
			GPSListener gpsl = listeners.get(i);
			gpsl.sentenceReceived(sen);
		}
	}

	/**
	 * add a listener to manage events with GPS
	 * 
	 * @param listener
	 */
	public static synchronized void addListener (GPSListener listener){
		listeners.add(listener); 
	}

	/**
	 * Remove a listener
	 * 
	 * @param listener
	 */
	public static synchronized void removeListener (GPSListener listener)
	{
		listeners.remove(listener); 
	}

	/*
	 * THREAD SECTION
	 * 
	 */
	
	/**
	 * Keeps reading sentences from GPS receiver stream and extracting data.
	 * This is a daemon thread so when program ends it won't keep running.
	 */
	public void run() {
		while(!close) {
			String s = getNextString();
			System.out.println("Sentence: " + s);
			
			// Check if sentence is valid:
			if (!s.startsWith("$"))
				continue;
			
			int p = s.lastIndexOf('*');
			if (p < 0) 
				continue;
			
			//XOR all characters between $ and *
			int checksum1 = 0;
			for (int i=1; i<p; i++)
				checksum1 ^= s.charAt(i);
			
			try{
				int checksum2 = Integer.parseInt(s.substring(p+1), 16);
				if (checksum1 != checksum2)
					continue;
				
				s = s.substring(0, p);
				int comma = s.indexOf(',');
				String token = s.substring(0,comma);

				//System.out.println("Token: " + token);
									
				sentenceChooser(token, s);
				
				//System.out.println(s);
				
			} catch(NoSuchElementException e) {
				//System.out.println("GPS: NoSuchElementException");				
			} catch(StringIndexOutOfBoundsException e) {
				//System.out.println("GPS: StringIndexOutOfBoundsException");
			} catch(ArrayIndexOutOfBoundsException e) {
				//System.out.println("GPS: ArrayIndexOutOfBoundsException");
			} catch(Exception e) {
				//System.out.println("GPS: Exception");
			}
		}
	}

	
	/**
	 * Pulls the next NMEA sentence as a string
	 * @return NMEA string, including $ and end checksum 
	 */
	private String getNextString() {
		StringBuilder currentSentence = new StringBuilder();
		try {
			int c;
			
			// ignore leading CR/LF
			do {
				c = in.read();
			} while (c == '\n' || c == '\r');
			
			// trailing EOF marks EOL, leading EOF shall yield ""
			while (c >= 0) {
				currentSentence.append((char)c);
				c = in.read();
				
				// trailing CR/LF marks EOL
				if (c == '\n' || c == '\r')
					break;
			}
		} catch (IOException e) {
			//TODO handle errors
		}
		return currentSentence.toString();
	}

	/**
	 * Internal helper method to aid in the subclass architecture. Overwritten by subclass.
	 * @param header
	 * @param s
	 */
	protected void sentenceChooser(String header, String s) {
		if (header.equals(GGASentence.HEADER)){
			this.ggaSentence.parse(s);
			notifyListeners(this.ggaSentence);
		}else if (header.equals(VTGSentence.HEADER)){
			this.vtgSentence.parse(s);
			notifyListeners(this.vtgSentence);
		}else if (header.equals(GSASentence.HEADER)){
			gsaSentence.parse(s);
			notifyListeners(this.gsaSentence);
		}
	}
}
