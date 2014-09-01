package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Modes in which the sensor can operate.
 */
public enum GyroSensorMode implements SensorsMode {
    RATE(), ANGLE(), GET_MODE(), GET_SAMPLE(), RESET();

    private final String[] values;

    private GyroSensorMode(String... values) {
        this.values = values;
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
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