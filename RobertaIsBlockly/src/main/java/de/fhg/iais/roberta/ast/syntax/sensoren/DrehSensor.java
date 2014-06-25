package de.fhg.iais.roberta.ast.syntax.sensoren;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class DrehSensor extends Sensor {
    private final Mode mode;
    private final ActorPort motor;

    private DrehSensor(Mode mode, ActorPort motor) {
        super(Phrase.Kind.DrehSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.motor = motor;
        setReadOnly();
    }

    public static DrehSensor make(Mode mode, ActorPort motor) {
        return new DrehSensor(mode, motor);
    }

    public Mode getMode() {
        return this.mode;
    }

    public ActorPort getMotor() {
        return this.motor;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.motor + ")");
    }

    @Override
    public String toString() {
        return "DrehSensor [mode=" + this.mode + ", motor=" + this.motor + "]";
    }

    public static enum Mode {
        ROTATION(), DEGREE(), GET_MODE(), GET_SAMPLE(), RESET();

        private final String[] values;

        private Mode(String... values) {
            this.values = values;
        }

        public static Mode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Mode mo : Mode.values() ) {
                if ( mo.toString().equals(sUpper) ) {
                    return mo;
                }
                for ( String value : mo.values ) {
                    if ( sUpper.equals(value) ) {
                        return mo;
                    }
                }
            }
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }
}
