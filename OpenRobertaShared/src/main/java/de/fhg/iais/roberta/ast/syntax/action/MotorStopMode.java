package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Mode in which the motor can stop.
 */
public enum MotorStopMode {
    NONFLOAT(), FLOAT();
    private final String[] values;

    private MotorStopMode(String... values) {
        this.values = values;
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * Get stopping mode from {@link MotorStopMode} from string parameter. It is possible for one stopping mode to have multiple string mappings.
     * Throws exception if the stopping mode does not exists.
     * 
     * @param name of the stopping mode
     * @return name of the stopping mode from the enum {@link MotorStopMode}
     */
    public static MotorStopMode get(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid stopping mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( MotorStopMode md : MotorStopMode.values() ) {
            if ( md.toString().equals(sUpper) ) {
                return md;
            }
            for ( String value : md.values ) {
                if ( sUpper.equals(value) ) {
                    return md;
                }
            }
        }
        throw new DbcException("Invalid stopping mode: " + mode);
    }
}
