package lejos.hardware.gps;

import java.io.InputStream;
import java.util.Date;
  
/**
 * This class manages data received from a GPS Device.
 * GPS Class manages the following NMEA Sentences:
 *  
 * GPRMC
 * GPGSV
 * GPGSA
 * GPGGA (superclass)
 * GPVTG (superclass)
 * 
 * @author BB
 * @author Juan Antonio Brenha Moral
 *
 */
/*
 * DEVELOPER NOTES: More NMEA sentence types that can be added if there is demand for them:
 * http://www.gpsinformation.org/dale/nmea.htm
 */
public class GPS extends SimpleGPS {
	
	//Classes which manages GGA, RMC, VTG, GSV, GSA Sentences
	private RMCSentence rmcSentence;
	//TODO device sends a sequence of complementary gsv sentences
	// this class only remembers the last one
	private GSVSentence gsvSentence;

	//Date Object with use GGA & RMC Sentence
	private Date date;
	
	/**
	 * The constructor. It needs an InputStream
	 * 
	 * @param in An input stream from the GPS receiver
	 */
	public GPS(InputStream in) {
		super(in);
		rmcSentence = new RMCSentence();
		gsvSentence = new GSVSentence();
		
		date = new Date();
	}
	

	/* GETTERS & SETTERS */

	/**
	 * Return Compass Degrees
	 * in a range: 0.0-359.9
	 * 
	 * @return the compass degrees
	 */
	public float getCompassDegrees(){
		return rmcSentence.getCompassDegrees();	
	}
	
	/**
	 * Return a Date Object with data from GGA and RMC NMEA Sentence
	 * 
	 * @return the date
	 */
	public Date getDate(){
		// TODO: Would be more proper to return a new Date object instead of recycled Date.
		updateDate();
		updateTime();
		return date;
	}

	/**
	 * 
	 * Get NMEA Satellite. The satellite list is retrieved from the almanac data. Satellites are
	 * ordered by their elevation: highest elevation (index 0) -> lowest elevation.
	 * 
	 * @param index the satellite index
	 * @return the NMEASaltellite object for the selected satellite
	 */
	public Satellite getSatellite(int index){
		Satellite s = gsvSentence.getSatellite(index); 
		// Compare getPRN() with this satellite, fill in setTracked():
		// TODO: This fails because most satellites are set to 0 when this is called. Not synced yet.
		boolean tracked = false;
		int [] prns = getPRN();
		for(int i=0;i<prns.length;i++) {
			if(prns[i] == s.getPRN()) {
				tracked=true;
				break;
			}
		}
		s.setTracked(tracked);
		return s;
	}
	
	
	/* TODO: Might be worth overwriting the SimpleGPS method for lat, long, speed, course, 
	and maybe time because they can be gotten from two sources (RMC). Perhaps check if
	== -1, if so try getting it from another sentence. Also check time-stamp for both to 
	see which is more recent. */
	/* ANSWER: With Holux-1200, GGA gets values before RMC. Ignore RMC? */ 
	
	/**
	 * Returns the number of satellites being tracked to
	 * determine the coordinates. This method overwrites the superclass method
	 * and returns the number from the GSV sentence.
	 * 
	 * @return Number of satellites e.g. 8
	 */
	public int getSatellitesTracked(){
		return ggaSentence.getSatellitesTracked();
	}
	
	/**
	 * The satellites in view is a list of satellites the GPS could theoretically connect to (i.e. satellites that 
	 * are not over the earth's horizon). The getSatellitesInView() method will always return an equal or greater
	 * number than getSatellitesTracked().
	 * 
	 * @return Number of satellites e.g. 8
	 */
	public int getSatellitesInView(){
		return gsvSentence.getSatellitesInView();
	}
	
		
	/**
	 * Internal helper method to aid in the subclass architecture. Overwrites the superclass
	 * method and calls it internally.
	 * 
	 * @param header
	 * @param s
	 */
	protected void sentenceChooser(String header, String s) {
		if (header.equals(RMCSentence.HEADER)){
			rmcSentence.parse(s);
			notifyListeners(this.rmcSentence);
		}else if (header.equals(GSVSentence.HEADER)){
			gsvSentence.parse(s);
			notifyListeners(this.gsvSentence);
		} else{
			super.sentenceChooser(header, s); // Check superclass sentences.
		}
	}
	
	/* NMEA */

	/**
	 * Update Time values
	 */
	private void updateTime(){
		
		int timeStamp = ggaSentence.getTime();
		
		if(timeStamp >0) {
			int hh = timeStamp / 10000;
			int mm = (timeStamp / 100) % 100;
			int ss = timeStamp % 100;
		
			date.setHours(hh);
			date.setMinutes(mm);
			date.setSeconds(ss);
		}
	}

	/**
	 * Update Date values
	 */
	private void updateDate(){
		int dateStamp = rmcSentence.getDate();
		
		if(dateStamp > 0) {
			int dd = dateStamp / 10000;
			int mm = (dateStamp / 100) % 100;
			int yy = dateStamp % 100;
			
			date.setDate(dd);
			date.setMonth(mm);
			date.setYear(yy);
		}
	}
}