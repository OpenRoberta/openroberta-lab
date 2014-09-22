package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

public enum MotorSide {
    RIGHT(), LEFT(), NONE();

    private final String[] values;

    private MotorSide(String... values) {
        this.values = values;
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * Get the side of the motor from {@link MotorSide} from string parameter.
     * Throws exception if the motor side does not exists.
     * 
     * @param name of the side
     * @return name of the side from the enum {@link MotorSide}
     */
    public static MotorSide get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid motor side: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( MotorSide sp : MotorSide.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid motor side: " + s);
    }
}
