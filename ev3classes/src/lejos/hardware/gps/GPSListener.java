package lejos.hardware.gps;


import java.util.EventListener;

/**
 * This is the interface to manage events with GPS
 * 
 * @author Juan Antonio Brenha Moral
 *
 */

public interface GPSListener extends EventListener{
	
	// TODO: Probably just one sentenceReceived() method, with NMEA sentence.
	// Compare GGASentence.HEADER with the NMEASentence.getHeader() using .equals;
	/**
	 * Called whenever a new NMEA sentence is produced by the GPS receiver.
	 * To identify the type of NMEA sentence received, use NMEASentence.getHeader().
	 * Then cast the sentence into the appropriate type. e.g. (GGASentence)sen
	 */
	public void sentenceReceived(NMEASentence sen);

}
