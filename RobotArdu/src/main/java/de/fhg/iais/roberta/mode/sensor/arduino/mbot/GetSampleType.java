package de.fhg.iais.roberta.mode.sensor.arduino.mbot;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum GetSampleType {
    TOUCH( "SENSORPORT", "", "TOUCH" ),
    ULTRASONIC_DISTANCE( "SENSORPORT", "ULTRASONIC", "DISTANCE" ),
    ULTRASONIC_PRESENCE( "SENSORPORT", "ULTRASONIC", "PRESENCE" ),
    TIME( "", "", "TIME" ),
    TEMPERATURE( "SENSORPORT", "", "TEMPERATURE" ),
    LIGHT( "SENSORPORT", "", "SENSOR_AMBIENTLIGHT" ),
    AMBIENTLIGHT( "SENSORPORT", "", "SENSOR_AMBIENTLIGHT" ),
    GYRO( "SENSORPORT", "MODE", "GYRO_ANGLE" ),
    ACCELEROMETER( "SENSORPORT", "MODE", "ACCELEROMETER" ),
    JOYSTICK( "SENSORPORT", "MODE", "SENSOR_JOYSTICK" ),
    SENSOR_FLAME( "SENSORPORT", "", "SENSOR_FLAME" ),
    SENSOR_PIRMOTION( "SENSORPORT", "", "SENSOR_PIRMOTION" ),
    SENSOR_BATTERY( "SENSORPORT", "", "SENSOR_BATTERY" );
    private final String port;
    private final String mode;
    private final String sensorType;
    private final String[] values;

    private GetSampleType(String port, String mode, String sensorType, String... values) {
        this.values = values;
        this.port = port;
        this.mode = mode;
        this.sensorType = sensorType;
    }

    /**
     * @return arm side
     */
    public String getPort() {
        return this.port;
    }

    public String getSensorType() {
        return this.sensorType;
    }

    /**
     * @return arm part
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * @param arm side
     * @param arm part
     * @return sensor type
     */
    public static GetSampleType get(String s) {
        if ( s == null || s.isEmpty() ) {
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
