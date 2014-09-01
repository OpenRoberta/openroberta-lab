package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Modes in which the sensor can operate.
 * 
 * @author kcvejoski
 */
public enum UltrasonicSensorMode implements SensorsMode {
    DISTANCE(), PRESENCE(), GET_MODE(), GET_SAMPLE();

    private final String[] values;

    private UltrasonicSensorMode(String... values) {
        this.values = values;
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * get mode from {@link UltrasonicSensorMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     * 
     * @param name of the mode
     * @return mode from the enum {@link UltrasonicSensorMode}
     */
    public static UltrasonicSensorMode get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( UltrasonicSensorMode mo : UltrasonicSensorMode.values() ) {
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