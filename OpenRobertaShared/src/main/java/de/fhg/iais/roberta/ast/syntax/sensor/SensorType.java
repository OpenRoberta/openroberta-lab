package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum SensorType {
    TOUCH( "SENSORPORT", "touch sensor (gedrückt)" ), ULTRASONIC( "SENSORPORT", "ultrasonic sensor" ), COLOUR( "SENSORPORT", "colour sensor" ), INFRARED(
        "SENSORPORT",
        "infrared sensor" ), ENCODER( "MOTORPORT", "encoder" ), KEYS_PRESSED( "KEY", "brick button (gedrückt)" ), KEYS_PRESSED_RELEASED(
        "KEY",
        "brick button (geklickt)" ), GYRO( "SENSORPORT", "gyroscope" ), TIME( "SENSORNUM", "time" );
    private final String[] values;
    private final String portTypeName;

    private SensorType(String portTypeName, String... values) {
        this.values = values;
        this.portTypeName = portTypeName;
    }

    /**
     * @return type of the port
     */
    public String getPortTypeName() {
        return this.portTypeName;
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