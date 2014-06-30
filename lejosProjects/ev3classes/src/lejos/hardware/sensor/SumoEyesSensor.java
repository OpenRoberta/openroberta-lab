package lejos.hardware.sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;

/**
 * Java class for MINDSENSORS NXT SumoEyes (triple zone IR obstacle detector).
 *
 * @author Daniele Benedettelli
 * @version 1.0
 */
public class SumoEyesSensor extends AnalogSensor implements SensorConstants {

    protected final static long SWITCH_DELAY = 10;
	
    /** The Constant NO_DETECTION (0). */
    public final static int NO_DETECTION = 0;
    
    /** The Constant LEFT (1). */
    public final static int LEFT = 1;
    
    /** The Constant CENTER (2). */
    public final static int CENTER = 2;
    
    /** The Constant RIGHT (3). */
    public final static int RIGHT = 3;
        
    /** The long range. */
    private boolean longRange = false;

    /**
     * Default constructor.
     *
     * @param port the sensor port
     */
    public SumoEyesSensor(AnalogPort port) {
        super(port);
        port.setTypeAndMode(TYPE_LIGHT_INACTIVE,MODE_RAW);
    }


    /**
     * Default constructor.
     *
     * @param port the sensor port
     */
    public SumoEyesSensor(Port port) {
        super(port);
        this.port.setTypeAndMode(TYPE_LIGHT_INACTIVE,MODE_RAW);
    }

    /**
     * Gets the raw value of the sensor.
     *
     * @return the raw sensor value
     */
    public int getValue() {
        return NXTRawIntValue(port.getPin1());
    }

    /**
     * Returns the detected zone (NO_DETECTION (0) , RIGHT (1), CENTER (2), LEFT (3))
     * @return detected zone constant
     *
     */
    public int getObstacle() {
        int value = NXTRawIntValue(port.getPin1());
        if (value == 1023) {
            return NO_DETECTION;
        }
        if (value > 300 && value < 400) {
            return CENTER;
        }
        if (value > 600) {
            return LEFT;
        }
        return RIGHT;
    }

    /**
     * Enables long range of the sensor.
     *
     * @param longRange if true, enables long range, if false enables short range
     */
    public void setLongRange(boolean longRange) {
        this.longRange = longRange;
        if (this.longRange) {
        	switchType(TYPE_LIGHT_ACTIVE, SWITCH_DELAY);
        } else
        	switchType(TYPE_LIGHT_INACTIVE, SWITCH_DELAY);
    }

    /**
     * Returns the current range of the sensor.
     *
     * @return current range of the sensor
     */
    public boolean isLongRange() {
        return longRange;
    }
}
