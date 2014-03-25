package lejos.hardware.sensor;

/**
 * Constants used to set Sensor types and modes.
 *
 */
public interface SensorConstants {

	/**
     * Colors used as the output value when in full mode. Values are
     * compatible with LEGO firmware.
     */
    public static final int BLACK = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int RED = 5;
    public static final int WHITE = 6;
    /** Color sensor data RED value index. */
    public static final int RED_INDEX = 0;
    /** Color sensor data GREEN value index. */
    public static final int GREEN_INDEX = 1;
    /** Color sensor data BLUE value index. */
    public static final int BLUE_INDEX = 2;
    /** Color sensor data BLANK/Background value index. */
    public static final int BLANK_INDEX = 3;
	
	public static final int TYPE_NO_SENSOR = 0;
	public static final int TYPE_SWITCH = 1;
	public static final int TYPE_TEMPERATURE = 2;
	public static final int TYPE_REFLECTION = 3;
	public static final int TYPE_ANGLE = 4;
	public static final int TYPE_LIGHT_ACTIVE = 5;
	public static final int TYPE_LIGHT_INACTIVE = 6;
	public static final int TYPE_SOUND_DB = 7; 
	public static final int TYPE_SOUND_DBA = 8;
	public static final int TYPE_CUSTOM = 9;
	public static final int TYPE_LOWSPEED = 10;
	public static final int TYPE_LOWSPEED_9V = 11;
    public static final int TYPE_HISPEED = 12;
    public static final int TYPE_COLORFULL = 13;
    public static final int TYPE_COLORRED = 14;
    public static final int TYPE_COLORGREEN = 15;
    public static final int TYPE_COLORBLUE = 16;
    public static final int TYPE_COLORNONE = 17;
    
    public static final int MIN_TYPE = 0;
    public static final int MAX_TYPE = 17;
	  
	public static final int MODE_RAW = 0x00;
	public static final int MODE_BOOLEAN = 0x20;
	public static final int MODE_TRANSITIONCNT = 0x40;
	public static final int MODE_PERIODCOUNTER = 0x60;
	public static final int MODE_PCTFULLSCALE = 0x80;
	public static final int MODE_CELSIUS = 0xA0;
	public static final int MODE_FARENHEIT = 0xC0;
	public static final int MODE_ANGLESTEP = 0xE0;

    /** MAX value returned as a RAW sensor reading for standard NXT A/D sensors */
    public static final int NXT_ADC_RES = 1023;
}
