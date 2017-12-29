package de.fhg.iais.roberta.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum GetSampleType {
    TOUCH( "SENSORPORT", "TOUCH", "TOUCH" ),
    TOUCH_PRESSED( "SENSORPORT", "TOUCH", "PRESSED" ),
    ULTRASONIC_DISTANCE( "SENSORPORT", "ULTRASONIC", "DISTANCE" ),
    ULTRASONIC_PRESENCE( "SENSORPORT", "ULTRASONIC", "PRESENCE" ),
    COLOUR_COLOUR( "SENSORPORT", "COLOUR", "COLOUR" ),
    COLOUR_LIGHT( "SENSORPORT", "COLOUR", "LIGHT" ),
    COLOUR_AMBIENTLIGHT( "SENSORPORT", "COLOUR", "AMBIENTLIGHT" ),
    INFRARED_DISTANCE( "SENSORPORT", "INFRARED", "DISTANCE" ),
    ENCODER_ROTATION( "SENSORPORT", "ENCODER", "ROTATION" ),
    ENCODER_DEGREE( "SENSORPORT", "ENCODER", "DEGREE" ),
    ENCODER_DISTANCE( "SENSORPORT", "ENCODER", "DISTANCE" ),
    KEYS_PRESSED( "SENSORPORT", "KEYS_PRESSED", "PRESSED" ),
    KEY_PRESSED( "SENSORPORT", "KEYS_PRESSED", "PRESSED" ),
    GYRO_RATE( "SENSORPORT", "GYRO", "RATE" ),
    GYRO_ANGLE( "SENSORPORT", "GYRO", "ANGLE" ),
    TIME( "SENSORPORT", "TIME", "VALUE" ),
    TIMER_VALUE( "SENSORPORT", "TIME", "VALUE" ),
    SOUND( "SENSORPORT", "SOUND", "VALUE" ),
    SOUND_SOUND( "SENSORPORT", "SOUND", "SOUND" ),
    LIGHT_LIGHT( "SENSORPORT", "LIGHT", "LIGHT" ),
    LIGHT_AMBIENTLIGHT( "SENSORPORT", "LIGHT", "AMBIENTLIGHT" ),
    INFRARED_OBSTACLE( "SENSORPORT", "INFRARED", "OBSTACLE" ),
    INFRARED_PRESENCE( "SENSORPORT", "INFRARED", "PRESENCE" ),
    COMPASS_ANGLE( "SENSORPORT", "COMPASS", "ANGLE" ),
    INFRARED_SEEK( "SENSORPORT", "INFRARED", "SEEK" ),
    PINTOUCH_PRESSED( "SENSORPORT", "PINTOUCH", "PRESSED" ),
    GESTURE_UP( "SENSORPORT", "GESTURE", "UP" ),
    GESTURE_FACE_UP( "SENSORPORT", "GESTURE", "FACE_UP" ),
    GESTURE_FACE_DOWN( "SENSORPORT", "GESTURE", "FACE_DOWN" ),
    GESTURESHAKE( "SENSORPORT", "GESTURE", "SHAKE" ),
    GESTURE_FREEFALL( "SENSORPORT", "GESTURE", "FREEFALL" ),
    GESTURE_ACTIVE( "GESTURE", "GESTURE_ACTIVE", "GESTURE_ACTIVE" ),
    MICROPHONE( "", "MICROPHONE", "MICROPHONE" ),
    TEMPERATURE( "", "TEMPERATURE", "TEMPERATURE" ),
    LIGHT_LEVEL( "", "LIGHT_LEVEL", "LIGHT_LEVEL" ),
    LIGHT_VALUE( "SENSORPORT", "LIGHT_VALUE", "LIGHT_VALUE" ),
    ACCELERATION( "DIRECTION", "ACCELERATION", "ACCELERATION" ),
    ORIENTATION( "ORIENTATION", "ORIENTATION", "ORIENTATION" ),
    PIN_TOUCHED( "SENSORPORT", "PIN_TOUCHED", "PIN_TOUCHED" ),
    PIN_ANALOG( "SENSORPORT", "PIN_ANALOG", "ANALOG" ),
    PIN_DIGITAL( "SENSORPORT", "PIN_DIGITAL", "DIGITAL" ),
    PIN_PULSEHIGH( "SENSORPORT", "PIN_PULSEHIGH", "PULSEHIGH" ),
    PIN_PULSELOW( "SENSORPORT", "PIN_PULSELOW", "PULSELOW" ),
    PIN_PULSE_HIGH( "SENSORPORT", "PIN_PULSE_HIGH", "PULSE_HIGH" ),
    PIN_PULSE_LOW( "SENSORPORT", "PIN_PULSE_LOW", "PULSE_LOW" ),
    TEMPERATURE_VALUE( "SENSORPORT", "TEMPERATURE", "TEMPERATURE" ),
    ACCELEROMETER_VALUE( "SENSORPORT", "ACCELERATION", "ACCELERATION" );

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

    /**
     * get mode from {@link MotorTachoMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link MotorTachoMode}
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
