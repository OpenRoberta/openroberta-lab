package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.DbcException;

public class MotorDuration {
    private Mode type;
    private Expr value;

    public MotorDuration(Mode type, Expr value) {
        super();
        this.setType(type);
        this.value = value;
    }

    public Expr getValue() {
        return this.value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    public Mode getType() {
        return this.type;
    }

    public void setType(Mode type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MotorDuration [type=" + this.type + ", value=" + this.value + "]";
    }

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
