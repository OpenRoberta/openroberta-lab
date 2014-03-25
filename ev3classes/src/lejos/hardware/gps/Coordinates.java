package lejos.hardware.gps;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class has been designed to manage coordinates 
 * using JSR-179 Location API
 * http://www.jcp.org/en/jsr/detail?id=179
 * 
 * @author Juan Antonio Brenha Moral (calculateDistanceAndAzimuth by Charles Manning)
 */
public class Coordinates{
	private double latitude;
	private double longitude;
	private double altitude;
	
	/**
	 * Identifier for string coordinate representation Degrees, Minutes, decimal fractions of a minute
	 * See Also:Constant Field Values
	 */
	public static final int DD_MM=2;

	 /**
	  * Identifier for string coordinate representation Degrees, Minutes, Seconds and decimal fractions of a second
	 * See Also:Constant Field Values
	 */
	public static final int DD_MM_SS=1;

	static final double EARTH_RADIUS = 6378137D;
	
	static float calculatedDistance = Float.NaN;
	static float calculatedAzimuth = Float.NaN;
	
	/* Constructor */

	/**
	 * Create a Coordinate object with 3 parameters:
	 * latitude, longitude and altitude
	 * 
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 */
	public Coordinates(double latitude, double longitude,double altitude) {
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
	}

	public Coordinates(double latitude, double longitude) {
		this(latitude,longitude,0);
	}

	/**
	 * <p>Returns the latitude component of this coordinate. Positive values indicate northern 
	 * latitude and negative values southern latitude.</p>
	 * 
	 * <p>The latitude is given in WGS84 datum.</p>
	 * @return the latitude in degrees
	 * @see #setLatitude(double)
	 * 
	 */
	public double getLatitude() {
		return latitude;
	}

	/*
	 * Set Latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/*
	 * Set Longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 *  <p>Returns the longitude component of this coordinate. Positive values indicate eastern longitude 
	 *  and negative values western longitude.</p>
	 *  <p>The longitude is given in WGS84 datum.</p>
	 *  
	 *  @return the longitude in degrees
	 *  @see #setLongitude(double)
	 */
	public double getLongitude() {
		return longitude;
	}

	/*
	 * Set Altitude in meters
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	/**
	 * Altitude above mean sea level.
	 * 
	 * @return the altitude
	 */
	public double getAltitude() {
		return altitude;
	}

  /**
     * <p>Calculates the azimuth between the two points according to 
     * the ellipsoid model of WGS84. The azimuth is relative to true north.</p> 
     * 
     * <p>The Coordinates object on which this method is called is considered 
     * the origin for the calculation and the Coordinates object passed 
     * as a parameter is the destination which the azimuth is calculated to.</p>
     * 
     *  <p>The azimuth (in degrees) increases <b>clockwise</b>. On this coordinate system, north is 0 degrees, 
     *  east is 90 degrees, south is 180 degrees, and west is 270 degrees.</p>
     *  
     * <p>When the origin is the North pole and the destination 
     * is not the North pole, this method returns 180.0. 
     * When the origin is the South pole and the destination is not 
     * the South pole, this method returns 0.0. If the origin is equal 
     * to the destination, this method returns 0.0.</p> 
     * <p>The implementation shall calculate the result as exactly as it can. 
     * However, it is required that the result is within 1 degree of the correct result.</p>
     *
     */
	public double azimuthTo(Coordinates to){
		if(to == null){
			throw new NullPointerException();
		}else{
			// TODO: Is there some way to make it not recalculate if it already calculated for these coordinates? Keep in mind coordinates can change.
			// Perhaps it keeps a reference to last to coordinate. If values are the same, then doesn't recalculate.
			calculateDistanceAndAzimuth(getLatitude(), getLongitude(), to.getLatitude(), to.getLongitude());
			while (calculatedAzimuth < 0) calculatedAzimuth += 360;
			while(calculatedAzimuth >= 360) calculatedAzimuth -= 360;
			return calculatedAzimuth;
		}
	}
	
/***********************************
UNTESTED as of April 7, 2009 - BB
 ********************************* /
	
	/**
	 * Converts a double representation of a coordinate with decimal degrees into a string
	 * representation. There are string syntaxes supported are the same as for the
	 * #convert(String) method. The implementation shall provide as many significant
	 * digits for the decimal fractions as are allowed by the string syntax definition.
	 *
	 * @param coordinate
	 *            a double representation of a coordinate
	 * @param outputType
	 *            identifier of the type of the string representation wanted for output
	 *            The constant {@link #DD_MM_SS} identifies the syntax 1 and the constant
	 *            {@link #DD_MM} identifies the syntax 2.
	 * @throws IllegalArgumentException
	 *             if the outputType is not one of the two constant values defined in this
	 *             class or if the coordinate value is not within the range [-180.0,
	 *             180.0) or is Double.NaN
	 * @return a string representation of the coordinate in a representation indicated by
	 *         the parameter
	 * @see #convert(String)
	 */
	public static String convert(double coordinate, int outputType)
			throws IllegalArgumentException {
		if ((coordinate < -180.0) || (coordinate >= 180.0) || (coordinate != coordinate))
			throw new IllegalArgumentException();

		// 14 is max length, example: -123:12:12.123
		StringBuilder sb = new StringBuilder(14);
		
		if (coordinate < 0)
		{
			coordinate = -coordinate;
			sb.append("-");
		}

		int r;
		switch (outputType)
		{
			case DD_MM:
			{
				//convert to milli-minutes
				r = (int)(coordinate * 60000 + 0.5);
				sb.append(r / 60000);
				break;
			}
			case DD_MM_SS:
			{
				//convert to milli-seconds
				r = (int)(coordinate * 3600000 + 0.5);
				sb.append(r / 3600000);
				sb.append(':');
				
				sb.append((char)('0' + r / 600000 % 6));
				sb.append((char)('0' + r / 60000 % 10));
				break;
			}
			default:
				throw new IllegalArgumentException();
		}
		
		sb.append(':');
		sb.append((char)('0' + r / 10000 % 6));
		sb.append((char)('0' + r / 1000 % 10));
		
		r = r % 1000;
		if (r != 0)
		{
			sb.append('.');
			do
			{
				sb.append((char)('0' + r / 100));
				r = r * 10 % 1000;
			} while (r != 0);
		}
		
		return sb.toString(); 
	}

	/**
	 * Takes an integer and removes trailing zeros.
	 *
	 * @param number
	 *            must be positive
	 * @return the number as a String, with trailing zeros removed. Returns null if the
	 *         number was zero or negative.
	 */
	private static String dropTrailingZeros(int number) {
		if (number <= 0)
			return null;
		while ((number % 10) == 0) {
			number = number / 10;
		}
		return Integer.toString(number);
	}

	/**
	 * Converts a String representation of a coordinate into the double representation as
	 * used in this API. There are two string syntaxes supported:
	 * <p>
	 * 1. Degrees, minutes, seconds and decimal fractions of seconds. This is expressed as
	 * a string complying with the following BNF definition where the degrees are within
	 * the range [-179, 179] and the minutes and seconds are within the range [0, 59], or
	 * the degrees is -180 and the minutes, seconds and decimal fractions are 0:
	 * <p>
	 * coordinate = degrees &quot;:&quot; minutes &quot;:&quot; seconds &quot;.&quot;
	 * decimalfrac | degrees &quot;:&quot; minutes &quot;:&quot; seconds | degrees
	 * &quot;:&quot; minutes<br />
	 * degrees = degreedigits | &quot;-&quot; degreedigits<br />
	 * degreedigits = digit | nonzerodigit digit | &quot;1&quot; digit digit<br />
	 * minutes = minsecfirstdigit digit<br />
	 * seconds = minsecfirstdigit digit<br />
	 * decimalfrac = 1*3digit <br />
	 * digit = &quot;0&quot; | &quot;1&quot; | &quot;2&quot; | &quot;3&quot; |
	 * &quot;4&quot; | &quot;5&quot; | &quot;6&quot; | &quot;7&quot; | &quot;8&quot; |
	 * &quot;9&quot;<br />
	 * nonzerodigit = &quot;1&quot; | &quot;2&quot; | &quot;3&quot; | &quot;4&quot; |
	 * &quot;5&quot; | &quot;6&quot; | &quot;7&quot; | &quot;8&quot; | &quot;9&quot;<br />
	 * minsecfirstdigit = &quot;0&quot; | &quot;1&quot; | &quot;2&quot; | &quot;3&quot; |
	 * &quot;4&quot; | &quot;5&quot;<br />
	 * <p>
	 * 2. Degrees, minutes and decimal fractions of minutes. This is expressed as a string
	 * complying with the following BNF definition where the degrees are within the range
	 * [-179, 179] and the minutes are within the range [0, 59], or the degrees is -180
	 * and the minutes and decimal fractions are 0:
	 * <p>
	 * coordinate = degrees &quot;:&quot; minutes &quot;.&quot; decimalfrac | degrees
	 * &quot;:&quot; minutes<br/> degrees = degreedigits | &quot;-&quot; degreedigits<br/>
	 * degreedigits = digit | nonzerodigit digit | &quot;1&quot; digit digit<br/> minutes =
	 * minsecfirstdigit digit<br/> decimalfrac = 1*5digit<br/> digit = &quot;0&quot; |
	 * &quot;1&quot; | &quot;2&quot; | &quot;3&quot; | &quot;4&quot; | &quot;5&quot; |
	 * &quot;6&quot; | &quot;7&quot; | &quot;8&quot; | &quot;9&quot;<br/> nonzerodigit =
	 * &quot;1&quot; | &quot;2&quot; | &quot;3&quot; | &quot;4&quot; | &quot;5&quot; |
	 * &quot;6&quot; | &quot;7&quot; | &quot;8&quot; | &quot;9&quot;<br/>
	 * minsecfirstdigit = &quot;0&quot; | &quot;1&quot; | &quot;2&quot; | &quot;3&quot; |
	 * &quot;4&quot; | &quot;5&quot;
	 * <p>
	 * For example, for the double value of the coordinate 61.51d, the corresponding
	 * syntax 1 string is "61:30:36" and the corresponding syntax 2 string is "61:30.6".
	 *
	 * @param coordinate
	 *            a String in either of the two representation specified above
	 * @return a double value with decimal degrees that matches the string representation
	 *         given as the parameter
	 * @throws IllegalArgumentException
	 *             if the coordinate input parameter does not comply with the defined
	 *             syntax for the specified types
	 * @throws NullPointerException
	 *             if the coordinate string is null convert
	 */
	// TODO: This method similar to NMEASentence.degreesMintoDegrees(). Use that? 
	public static double convert(String coordinate)
			throws IllegalArgumentException, NullPointerException {
		/*
		 * A much more academic way to do this would be to generate some tree-based parser
		 * code using the BNF definition, but that seems a little too heavyweight for such
		 * short strings.
		 */
		if (coordinate == null)
			throw new NullPointerException();

		/*
		 * We don't have Java 5 regex or split support in Java 1.3, making this task a bit
		 * of a pain to code.
		 */

		/*
		 * First we check that all the characters are valid, whilst also counting the
		 * number of colons and decimal points (we check that colons do not follow
		 * decimals). This allows us to know what type the string is.
		 */
		int length = coordinate.length();
		int colons = 0;
		int decimals = 0;
		for (int i = 0; i < length; i++) {
			char element = coordinate.charAt(i);
			if (!convertIsValidChar(element))
				throw new IllegalArgumentException();
			if (element == ':') {
				if (decimals > 0)
					throw new IllegalArgumentException();
				colons++;
			} else if (element == '.') {
				decimals++;
				if (decimals > 1)
					throw new IllegalArgumentException();
			}
		}

		/*
		 * Then we break the string into its components and parse the individual pieces
		 * (whilst also doing bounds checking). Code looks ugly because there is a lot of
		 * Exception throwing for bad syntax.
		 */
		String[] parts = convertSplit(coordinate);

		try {
			double out = 0.0;
			// the first 2 parts are the same, regardless of type
			int degrees = Integer.valueOf(parts[0]).intValue();
			if ((degrees < -180) || (degrees > 179))
				throw new IllegalArgumentException();
			boolean negative = false;
			if (degrees < 0) {
				negative = true;
				degrees = Math.abs(degrees);
			}

			out += degrees;

			int minutes = Integer.valueOf(parts[1]).intValue();
			if ((minutes < 0) || (minutes > 59))
				throw new IllegalArgumentException();
			out += minutes * 0.1 / 6;

			if (colons == 2) {
				// type 1
				int seconds = Integer.valueOf(parts[2]).intValue();
				if ((seconds < 0) || (seconds > 59))
					throw new IllegalArgumentException();
				// degrees:minutes:seconds
				out += seconds * 0.01 / 36;
				if (decimals == 1) {
					// degrees:minutes:seconds.decimalfrac
					double decimalfrac = Double.valueOf("0." + parts[3])
							.doubleValue();
					// note that spec says this should be 1*3digit, but we don't
					// restrict the digit count
					if ((decimalfrac < 0) || (decimalfrac >= 1))
						throw new IllegalArgumentException();
					out += decimalfrac * 0.01 / 36;
				}
			} else if ((colons == 1) && (decimals == 1)) {
				// type 2
				// degrees:minutes.decimalfrac
				double decimalfrac = Double.valueOf("0." + parts[2])
						.doubleValue();
				// note that spec says this should be 1*5digit, but we don't
				// restrict the digit count
				if ((decimalfrac < 0) || (decimalfrac >= 1))
					throw new IllegalArgumentException();
				out += decimalfrac * 0.1 / 6;
			} else
				throw new IllegalArgumentException();

			if (negative) {
				out = -out;
			}

			// do a final check on bounds
			if ((out < -180.0) || (out >= 180.0))
				throw new IllegalArgumentException();
			return out;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Helper method for {@link #convert(String)}
	 *
	 * @param element
	 * @return
	 */
	private static boolean convertIsValidChar(char element) {
		if ((element == '-') || (element == ':') || (element == '.')
				|| Character.isDigit(element))
			return true;
		return false;
	}

	/**
	 * Helper method for {@link #convert(String)}
	 *
	 * @param in
	 * @return
	 */
	private static String[] convertSplit(String in)
			throws IllegalArgumentException {
		Vector<String> parts = new Vector<String>(4);

		int start = 0;
		int length = in.length();
		for (int i = 0; i <= length; i++) {
			if ((i == length) || (in.charAt(i) == ':') || (in.charAt(i) == '.')) {
				// syntax checking
				if (start - i == 0)
					throw new IllegalArgumentException();
				String part = in.substring(start, i);
				parts.addElement(part);
				start = i + 1;
			}
		}

		// syntax checking
		if ((parts.size() < 2) || (parts.size() > 4))
			throw new IllegalArgumentException();
		// return an array
		String[] partsArray = new String[parts.size()];
		Enumeration<String> en = parts.elements();
		for (int i = 0; en.hasMoreElements(); i++) {
			partsArray[i] = en.nextElement();
		}
		return partsArray;
	}

/***********************************/	
	
	/**
	 *
     * Calculates the geodetic distance between the two points according 
     * to the ellipsoid model of WGS84. Altitude is neglected from calculations.
     * 
     * The implementation shall calculate this as exactly as it can. 
     * However, it is required that the result is within 0.36% of 
     * the correct result.
	 * 
	 * @param to the point to calculate the geodetic to
	 * @return the distance in meters
	 */
	public double distance(Coordinates to){
		if(to == null){
			throw new NullPointerException();
		}else{
			// TODO: Is there some way to make it not recalculate if it already 
			// calculated for these coordinates? Keep in mind coordinates can change.
			calculateDistanceAndAzimuth(getLatitude(), getLongitude(), to.getLatitude(), to.getLongitude());
			return calculatedDistance;
		}
	}

	private static void calculateDistanceAndAzimuth(double d, double d1, double d2, double d3){
        // TODO: This code is huge. Can it be minimized?
		double d4 = Math.toRadians(d);
        double d5 = Math.toRadians(d1);
		double d6 = Math.toRadians(d2);
		double d7 = Math.toRadians(d3);
		double d8 = 0.0033528106647474805D;
		// TODO: Why are these given 0 values?
        double d9 = 0.0D;
        double d10 = 0.0D;
        double d20 = 0.0D;
        double d22 = 0.0D;
        double d24 = 0.0D;
        double d25 = 0.0D;
        double d26 = 0.0D;
        double d28 = 0.0D;
        double d29 = 0.0D;
        double d30 = 0.0D;
        double d31 = 0.0D;
        double d32 = 0.0D;
        double d33 = 5.0000000000000003E-10D;
        int i = 1;
        byte byte0 = 100;
        if(d4 == d6 && (d5 == d7 || Math.abs(Math.abs(d5 - d7) - 6.2831853071795862D) < d33))
        {
            calculatedDistance = 0.0F;
            calculatedAzimuth = 0.0F;
            return;
        }
        
        // TODO: Use our version of Math.PI throughout, including 2pi.
        if(d4 + d6 == 0.0D && Math.abs(d5 - d7) == 3.1415926535897931D)
            d4 += 1.0000000000000001E-05D;
        double d11 = 1.0D - d8;
        double d12 = d11 * Math.tan(d4);
        double d13 = d11 * Math.tan(d6);
        double d14 = 1.0D / Math.sqrt(1.0D + d12 * d12);
        double d15 = d14 * d12;
        double d16 = 1.0D / Math.sqrt(1.0D + d13 * d13);
        double d17 = d14 * d16;
        double d18 = d17 * d13;
        double d19 = d18 * d12;
        d9 = d7 - d5;
        
        for(d32 = d9 + 1.0D; i < byte0 && Math.abs(d32 - d9) > d33; d9 = ((1.0D - d31) * d9 * d8 + d7) - d5)
        {
            i++;
            double d21 = Math.sin(d9);
            double d23 = Math.cos(d9);
            d12 = d16 * d21;
            d13 = d18 - d15 * d16 * d23;
            d24 = Math.sqrt(d12 * d12 + d13 * d13);
            d25 = d17 * d23 + d19;
            d10 = Math.atan2(d24, d25);
            double d27 = (d17 * d21) / d24;
            d28 = 1.0D - d27 * d27;
            d29 = 2D * d19;
            if(d28 > 0.0D)
                d29 = d25 - d29 / d28;
            d30 = -1D + 2D * d29 * d29;
            d31 = (((-3D * d28 + 4D) * d8 + 4D) * d28 * d8) / 16D;
            d32 = d9;
            d9 = ((d30 * d25 * d31 + d29) * d24 * d31 + d10) * d27;
        }
        
        double d34 = mod(Math.atan2(d12, d13), 6.2831853071795862D);
        d9 = Math.sqrt((1.0D / (d11 * d11) - 1.0D) * d28 + 1.0D);
        d9++;
        d9 = (d9 - 2D) / d9;
        d31 = ((d9 * d9) / 4D + 1.0D) / (1.0D - d9);
        d32 = (d9 * d9 * 0.375D - 1.0D) * d9;
        d9 = d30 * d25;
        
        double d35 = ((((((d24 * d24 * 4D - 3D) * (1.0D - d30 - d30) * d29 * d32) / 6D - d9) * d32) / 4D + d29) * d24 * d32 + d10) * d31 * 6378137D * d11;
        if((double)Math.abs(i - byte0) < d33)
        {
            calculatedDistance = (0.0F / 0.0F);
            calculatedAzimuth = (0.0F / 0.0F);
            return;
        }
        d34 = (180D * d34) / 3.1415926535897931D;
        calculatedDistance = (float)d35;
        calculatedAzimuth = (float)d34;
        if(d == 90D)
            calculatedAzimuth = 180F;
        else
        if(d == -90D)
            calculatedAzimuth = 0.0F;
    }

	// TODO: A mod method? Why not use %
    private static double mod(double d, double d1){
        return d - d1 * Math.floor(d / d1);
    }
    
}
