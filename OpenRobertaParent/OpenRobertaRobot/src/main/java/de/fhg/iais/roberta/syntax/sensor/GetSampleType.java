package de.fhg.iais.roberta.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum GetSampleType {
    TOUCH( "SENSORPORT", "TOUCH", "TOUCH", "SLOT" ),
    TOUCH_PRESSED( "SENSORPORT", "TOUCH", "PRESSED", "SLOT" ),
    ULTRASONIC_DISTANCE( "SENSORPORT", "ULTRASONIC", "DISTANCE", "SLOT" ),
    ULTRASONIC_PRESENCE( "SENSORPORT", "ULTRASONIC", "PRESENCE", "SLOT" ),
    COLOUR_COLOUR( "SENSORPORT", "COLOUR", "COLOUR", "SLOT" ),
    COLOUR_LIGHT( "SENSORPORT", "COLOUR", "LIGHT", "SLOT" ),
    COLOUR_AMBIENTLIGHT( "SENSORPORT", "COLOUR", "AMBIENTLIGHT", "SLOT" ),
    INFRARED_DISTANCE( "SENSORPORT", "INFRARED", "DISTANCE", "SLOT" ),
    ENCODER_ROTATION( "SENSORPORT", "ENCODER", "ROTATION", "SLOT" ),
    ENCODER_DEGREE( "SENSORPORT", "ENCODER", "DEGREE", "SLOT" ),
    ENCODER_DISTANCE( "SENSORPORT", "ENCODER", "DISTANCE", "SLOT" ),
    KEYS_PRESSED( "SENSORPORT", "KEYS_PRESSED", "PRESSED", "SLOT" ),
    KEY_PRESSED( "SENSORPORT", "KEYS_PRESSED", "PRESSED", "SLOT" ),
    GYRO_RATE( "SENSORPORT", "GYRO", "RATE", "SLOT" ),
    GYRO_ANGLE( "SENSORPORT", "GYRO", "ANGLE", "SLOT" ),
    GYRO_TILTED( "SENSORPORT", "GYRO", "TILTED", "SLOT" ),
    TIME( "SENSORPORT", "TIME", "VALUE", "SLOT" ),
    TIMER_VALUE( "SENSORPORT", "TIME", "VALUE", "SLOT" ),
    SOUND( "SENSORPORT", "SOUND", "VALUE", "SLOT" ),
    SOUND_SOUND( "SENSORPORT", "SOUND", "SOUND", "SLOT" ),
    LIGHT_LIGHT( "SENSORPORT", "LIGHT", "LIGHT", "SLOT" ),
    LIGHT_AMBIENTLIGHT( "SENSORPORT", "LIGHT", "AMBIENTLIGHT", "SLOT" ),
    INFRARED_OBSTACLE( "SENSORPORT", "INFRARED", "OBSTACLE", "SLOT" ),
    INFRARED_PRESENCE( "SENSORPORT", "INFRARED", "PRESENCE", "SLOT" ),
    COMPASS_ANGLE( "SENSORPORT", "COMPASS", "ANGLE", "SLOT" ),
    INFRARED_SEEK( "SENSORPORT", "INFRARED", "SEEK", "SLOT" ),
    PINTOUCH_PRESSED( "SENSORPORT", "PINTOUCH", "PRESSED", "SLOT" ),
    GESTURE_UP( "SENSORPORT", "GESTURE", "UP", "SLOT" ),
    GESTURE_DOWN( "SENSORPORT", "GESTURE", "DOWN", "SLOT" ),
    GESTURE_FACE_UP( "SENSORPORT", "GESTURE", "FACE_UP", "SLOT" ),
    GESTURE_FACE_DOWN( "SENSORPORT", "GESTURE", "FACE_DOWN", "SLOT" ),
    GESTURE_SHAKE( "SENSORPORT", "GESTURE", "SHAKE", "SLOT" ),
    GESTURE_FREEFALL( "SENSORPORT", "GESTURE", "FREEFALL", "SLOT" ),
    GESTURE_ACTIVE( "GESTURE", "GESTURE_ACTIVE", "GESTURE_ACTIVE", "SLOT" ),
    MICROPHONE( "", "MICROPHONE", "MICROPHONE", "SLOT" ),
    TEMPERATURE( "", "TEMPERATURE", "TEMPERATURE", "SLOT" ),
    LIGHT_LEVEL( "", "LIGHT_LEVEL", "LIGHT_LEVEL", "SLOT" ),
    LIGHT_VALUE( "SENSORPORT", "LIGHT_VALUE", "LIGHT_VALUE", "SLOT" ),
    ACCELERATION( "DIRECTION", "ACCELERATION", "ACCELERATION", "SLOT" ),
    ORIENTATION( "ORIENTATION", "ORIENTATION", "ORIENTATION", "SLOT" ),
    PIN_TOUCHED( "SENSORPORT", "PIN_TOUCHED", "PIN_TOUCHED", "SLOT" ),
    PIN_ANALOG( "SENSORPORT", "PIN_ANALOG", "ANALOG", "SLOT" ),
    PIN_DIGITAL( "SENSORPORT", "PIN_DIGITAL", "DIGITAL", "SLOT" ),
    PIN_PULSEHIGH( "SENSORPORT", "PIN_PULSEHIGH", "PULSEHIGH", "SLOT" ),
    PIN_PULSELOW( "SENSORPORT", "PIN_PULSELOW", "PULSELOW" ),
    PIN_PULSE_HIGH( "SENSORPORT", "PIN_PULSE_HIGH", "PULSE_HIGH", "SLOT" ),
    PIN_PULSE_LOW( "SENSORPORT", "PIN_PULSE_LOW", "PULSE_LOW", "SLOT" ),
    TEMPERATURE_VALUE( "SENSORPORT", "TEMPERATURE", "TEMPERATURE", "SLOT" ),
    ACCELEROMETER_VALUE( "SENSORPORT", "ACCELERATION", "ACCELERATION", "SLOT" ),
    ELECTRICCURRENT_VALUE( "SENSORPORT", "ELECTRIC_CURRENT", "VALUE", "SLOT" ),
    DETECTMARK_IDONE( "SENSORPORT", "DETECT_MARK", "IDONE", "SLOT" ),
    DETECTFACE_NAMEONE( "SENSORPORT", "DETECT_FACE", "NAMEONE", "SLOT" ),
    FSR_VALUE( "SENSORPORT", "FSR", "VALUE", "SLOT" ),
    WALL_DISTANCE( "SENSORPORT", "WALL", "DISTANCE", "SLOT" ),
    DROP_OFF_DISTANCE( "SENSORPORT", "DROP_OFF", "DISTANCE", "SLOT" ),
    INFRARED_AMBIENTLIGHT( "SENSORPORT", "LIGHT", "AMBIENTLIGHT", "SLOT" ),
    MOISTURE_VALUE( "SENSORPORT", "MOISTURE", "VALUE", "SLOT" ),
    POTENTIOMETER_VALUE( "SENSORPORT", "POTENTIOMETER", "VALUE", "SLOT" ),
    INFRARED_VALUE( "SENSORPORT", "INFRARED", "VALUE", "SLOT" ),
    HUMIDITY_HUMIDITY( "SENSORPORT", "HUMIDITY", "HUMIDITY", "SLOT" ),
    HUMIDITY_TEMPERATURE( "SENSORPORT", "HUMIDITY", "TEMPERATURE", "SLOT" ),
    MOTION_PRESENCE( "SENSORPORT", "MOTION", "PRESENCE", "SLOT" ),
    PULSE_VALUE( "SENSORPORT", "PULSE", "VALUE", "SLOT" ),
    DROP_VALUE( "SENSORPORT", "DROP", "VALUE", "SLOT" ),
    RFID_SERIAL( "SENSORPORT", "RFID", "SERIAL", "SLOT" ),
    RFID_PRESENCE( "SENSORPORT", "RFID", "PRESENCE", "SLOT" );
    private final String portTypeName;
    private final String sensorType;
    private final String sensorMode;
    private final String[] values;

    private GetSampleType(String portTypeName, String sensorType, String sensorMode, String... values) {
        this.values = values;
        this.portTypeName = portTypeName;
        this.sensorMode = sensorMode;
        this.sensorType = sensorType;
    }

    /**
     * @return type of the port
     */
    public String getPortTypeName() {
        return this.portTypeName;
    }

    public String getSensorType() {
        return this.sensorType;
    }

    /**
     * @return the sensorMode
     */
    public String getSensorMode() {
        return this.sensorMode;
    }

    public String[] getValues() {
        return this.values;
    }

    /**
     * get mode from {@link GetSampleType} from string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the mode
     * does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link GetSampleType}
     */
    public static GetSampleType get(String s) {
        if ( (s == null) || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( GetSampleType mo : GetSampleType.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.values ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid mode: " + s);
    }
}
