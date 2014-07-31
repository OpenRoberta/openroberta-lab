package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class is parameter class used to set the type of the motor duration mode {@link Mode} and value for the duration.
 */
public class MotorDuration {
    private Mode type;
    private Expr value;

    public MotorDuration(Mode type, Expr value) {
        super();
        this.setType(type);
        this.value = value;
    }

    /**
     * Returns the value of the duration set by the client.
     * 
     * @return value of the duration as {@link Expr}
     */
    public Expr getValue() {
        return this.value;
    }

    /**
     * Set the value of the motor duration.
     * 
     * @param value
     */
    public void setValue(Expr value) {
        this.value = value;
    }

    /**
     * Get the mode of the motor duration. See enum {@link Mode} for all possible modes.
     * 
     * @return mode of the motor duration.
     */
    public Mode getType() {
        return this.type;
    }

    /**
     * Set the mode of the motor duration. See enum {@link Mode} for all possible modes.
     */
    public void setType(Mode type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MotorDuration [type=" + this.type + ", value=" + this.value + "]";
    }

    /**
     * @author kcvejoski
     */
    public static enum Mode {
        ROTATIONS(), DEGREE(), DISTANCE();

        private final String[] values;

        private Mode(String... values) {
            this.values = values;
        }

        /**
         * get motor mode from {@link MotorDuration} from string parameter. It is possible for one motor mode to have multiple string mappings.
         * Throws exception if the mode does not exists.
         * 
         * @param name of the motor mode
         * @return motor mode from the enum {@link MotorDuration}
         */
        public static Mode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid motor mode: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Mode sp : Mode.values() ) {
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
}
