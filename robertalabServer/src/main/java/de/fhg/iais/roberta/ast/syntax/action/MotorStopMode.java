package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Mode in which the motor can operate
 * 
 * @author kcvejoski
 */
public enum MotorStopMode {
    NONFLOAT(), FLOAT();
    private final String[] values;

    private MotorStopMode(String... values) {
        this.values = values;
    }

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
