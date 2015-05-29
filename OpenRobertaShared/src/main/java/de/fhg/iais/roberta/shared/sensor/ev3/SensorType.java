package de.fhg.iais.roberta.shared.sensor.ev3;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum SensorType {
    TOUCH( "SENSORPORT", "TOUCH", "TOUCH" ),
    ULTRASONIC_DISTANCE( "SENSORPORT", "ULTRASONIC", "DISTANCE" ),
    ULTRASONIC_PRESENCE( "SENSORPORT", "ULTRASONIC", "PRESENCE" ),
    COLOUR_COLOUR( "SENSORPORT", "COLOUR", "COLOUR" ),
    COLOUR_LIGHT( "SENSORPORT", "COLOUR", "RED" ),
    COLOUR_AMBIENTLIGHT( "SENSORPORT", "COLOUR", "AMBIENTLIGHT" ),
    INFRARED_DISTANCE( "SENSORPORT", "INFRARED", "DISTANCE" ),
    ENCODER_ROTATION( "MOTORPORT", "ENCODER", "ROTATION" ),
    ENCODER_DEGREE( "MOTORPORT", "ENCODER", "DEGREE" ),
    KEYS_PRESSED( "KEY", "KEYS_PRESSED", "KEYS_PRESSED" ),
    GYRO_RATE( "SENSORPORT", "GYRO", "RATE" ),
    GYRO_ANGLE( "SENSORPORT", "GYRO", "ANGLE" ),
    TIME( "SENSORNUM", "TIME", "TIME" );

    private final String portTypeName;
    private final String sensorType;
    private final String sensorMode;
    private final String[] values;

    private SensorType(String portTypeName, String sensorType, String sensorMode, String... values) {
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
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * get mode from {@link MotorTachoMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link MotorTachoMode}
     */
    public static SensorType get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( SensorType mo : SensorType.values() ) {
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