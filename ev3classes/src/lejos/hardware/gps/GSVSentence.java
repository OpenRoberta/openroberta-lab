package lejos.hardware.gps;

import java.util.NoSuchElementException;

/**
 * This class has been designed to manage a GSV Sentence
 * 
 * GPS Satellites in view
 * 
 * eg. $GPGSV,3,1,11,03,03,111,00,04,15,270,00,06,01,010,00,13,06,292,00*74
 *     $GPGSV,3,2,11,14,25,170,00,16,57,208,39,18,67,296,40,19,40,246,00*74
 *     $GPGSV,3,3,11,22,42,067,42,24,14,311,43,27,05,244,00,,,,*4D
 * 
 * 
 *     $GPGSV,1,1,13,02,02,213,,03,-3,000,,11,00,121,,14,13,172,05*67
 * 
 * 1    = Total number of messages of this type in this cycle
 * 2    = Message number
 * 3    = Total number of SVs in view
 * 4    = SV PRN number
 * 5    = Elevation in degrees, 90 maximum
 * 6    = Azimuth, degrees from true north, 000 to 359
 * 7    = SNR, 00-99 dB (null when not tracking)
 * 8-11 = Information about second SV, same as field 4-7
 * 12-15= Information about third SV, same as field 4-7
 * 16-19= Information about fourth SV, same as field 4-7
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class GSVSentence extends NMEASentence{
	
	//GGA
	private String nmeaHeader = "";
	private int satellitesInView = 0;
	private final int MAXIMUMSATELLITES = 4;
	private Satellite ns1;
	private Satellite ns2;
	private Satellite ns3;
	private Satellite ns4;
	
	//Header
	public static final String HEADER = "$GPGSV";

	/*
	 * Constructor
	 */
	public GSVSentence(){
		ns1 = new Satellite();
		ns2 = new Satellite();
		ns3 = new Satellite();
		ns4 = new Satellite();
	}
	
	
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
	 * Returns the number of satellites being tracked to
	 * determine the coordinates.
	 * 
	 * @return Number of satellites e.g. 8
	 */
	public int getSatellitesInView() {
		return satellitesInView;
	}

	/**
	 * Return a NMEA Satellite object
	 * 
	 * @param index
	 * @return
	 */
	public Satellite getSatellite(int index){
		Satellite ns = new Satellite();
		if(index == 0){
			ns = ns1;
		}else if(index == 1){
			ns = ns2;
		}else if(index == 2){
			ns = ns3;
		}else if(index == 3){
			ns = ns4;
		}
		return ns;
	}

	
	/**
	 * Method used to parse a GSV Sentence
	 */
	public void parse(String sentence){

		String[] parts = sentence.split(",");
		
		int PRN = 0;
		int elevation = 0;
		int azimuth = 0;
		int SNR = 0;

		try{
			
			//Extracting data from a GSV Sentence
			
			//TODO Length of GSV Sentence varies.
			// See http://www.gpsinformation.org/dale/nmea.htm for an example
			
			nmeaHeader = parts[0];
			
			if (parts[3].length() == 0) {
				satellitesInView = 0;
			} else {
				satellitesInView = Math.round(Float.parseFloat(parts[3]));
			}
			
			if(satellitesInView > 0) {
				
				//SAT 1
				
				if(parts[4].length() == 0) {
					PRN = 0;
				} else{
					PRN = Math.round(Float.parseFloat(parts[4]));
				}

				if(parts[5].length() == 0) {
					elevation = 0;
				} else {
					elevation = Math.round(Float.parseFloat(parts[5]));
				}

				if (parts[6].length() == 0) {
					azimuth = 0;
				} else {
					azimuth = Math.round(Float.parseFloat(parts[6]));
				}

				if (parts[7].length() == 0) {
					SNR = 0;
				} else {
					SNR = Math.round(Float.parseFloat(parts[7]));
				}

				ns1.setPRN(PRN);
				ns1.setElevation(elevation);
				ns1.setAzimuth(azimuth);
				ns1.setSignalNoiseRatio(SNR);
				
				//SAT 2
				
				if (parts[8].length() == 0) {
					PRN = 0;
				} else {
					PRN = Math.round(Float.parseFloat(parts[8]));
				}

				if (parts[9].length() == 0) {
					elevation = 0;
				} else {
					elevation = Math.round(Float.parseFloat(parts[9]));
				}

				if (parts[10].length() == 0) {
					azimuth = 0;
				} else {
					azimuth = Math.round(Float.parseFloat(parts[10]));
				}

				if (parts[11].length() == 0) {
					SNR = 0;
				} else {
					SNR = Math.round(Float.parseFloat(parts[11]));
				}
				
				ns2.setPRN(PRN);
				ns2.setElevation(elevation);
				ns2.setAzimuth(azimuth);
				ns2.setSignalNoiseRatio(SNR);
				
				//SAT 3

				if (parts[12].length() == 0) {
					PRN = 0;
				} else {
					PRN = Math.round(Float.parseFloat(parts[12]));
				}

				if (parts[13].length() == 0) {
					elevation = 0;
				} else {
					elevation = Math.round(Float.parseFloat(parts[13]));
				}

				if (parts[14].length() == 0) {
					azimuth = 0;
				} else {
					azimuth = Math.round(Float.parseFloat(parts[14]));
				}

				if (parts[15].length() == 0) {
					SNR = 0;
				} else {
					SNR = Math.round(Float.parseFloat(parts[15]));
				}
				
				ns3.setPRN(PRN);
				ns3.setElevation(elevation);
				ns3.setAzimuth(azimuth);
				ns3.setSignalNoiseRatio(SNR);
				
				// SAT 4

				if (parts[16].length() == 0) {
					PRN = 0;
				} else {
					PRN = Math.round(Float.parseFloat(parts[16]));
				}

				if (parts[17].length() == 0) {
					elevation = 0;
				} else {
					elevation = Math.round(Float.parseFloat(parts[17]));
				}

				if (parts[18].length() == 0) {
					azimuth = 0;
				} else {
					azimuth = Math.round(Float.parseFloat(parts[18]));
				}

				if (parts[19].length() == 0) {
					SNR = 0;
				} else {
					SNR = Math.round(Float.parseFloat(parts[19]));
				}
				
				ns4.setPRN(PRN);
				ns4.setElevation(elevation);
				ns4.setAzimuth(azimuth);
				ns4.setSignalNoiseRatio(SNR);						
			}		
		} catch(NoSuchElementException e) {
			//System.err.println("GSVSentence: NoSuchElementException");
		} catch(NumberFormatException e) {
			//System.err.println("GSVSentence: NumberFormatException");
		} catch(Exception e) {
			//System.err.println("GSVSentence: Exception");
		}
	}//End parse	
}//End class
