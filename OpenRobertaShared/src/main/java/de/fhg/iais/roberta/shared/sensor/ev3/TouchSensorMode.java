package de.fhg.iais.roberta.shared.sensor.ev3;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Modes in which the sensor can operate.
 */
public enum TouchSensorMode implements SensorMode {
    TOUCH( "isPressed", "touch" );

    private final String halJavaMethodName;
    private final String[] values;

    private TouchSensorMode(String halJavaMethodName, String... values) {
        this.halJavaMethodName = halJavaMethodName;
        this.values = values;
    }

    public String getLejosModeName() {
        return this.values[0];
    }

    @Override
    public String getHalJavaMethod() {
        return this.halJavaMethodName;
    }

    /**
     * get mode from {@link TouchSensorMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link TouchSensorMode}
     */
    public static TouchSensorMode get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( TouchSensorMode mo : TouchSensorMode.values() ) {
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