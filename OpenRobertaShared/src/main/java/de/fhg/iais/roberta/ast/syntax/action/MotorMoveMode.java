package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This enum provides all possible motor types of movements.
 */
public enum MotorMoveMode {
    ROTATIONS(), DEGREE(), DISTANCE();

    private final String[] values;

    private MotorMoveMode(String... values) {
        this.values = values;
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * get motor mode from {@link MotorDuration} from string parameter. It is possible for one motor mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     * 
     * @param name of the motor mode
     * @return motor mode from the enum {@link MotorDuration}
     */
    public static MotorMoveMode get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid motor mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( MotorMoveMode sp : MotorMoveMode.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid motor mode: " + s);
    }
}