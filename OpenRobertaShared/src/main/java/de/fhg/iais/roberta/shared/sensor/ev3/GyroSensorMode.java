package de.fhg.iais.roberta.shared.sensor.ev3;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Modes in which the sensor can operate.
 */
public enum GyroSensorMode {
    RATE( "getGyroSensorValue", "Rate" ), ANGLE( "getGyroSensorValue", "Angle" ), RESET( "resetGyroSensor" );

    private final String halJavaMethodName;
    private final String[] values;

    private GyroSensorMode(String halJavaMethodName, String... values) {
        this.halJavaMethodName = halJavaMethodName;
        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */
    public String getLejosModeName() {
        return this.values[0];
    }

    public String getHalJavaMethod() {
        return this.halJavaMethodName;
    }

    /**
     * get mode from {@link GyroSensorMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link GyroSensorMode}
     */
    public static GyroSensorMode get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( GyroSensorMode mo : GyroSensorMode.values() ) {
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