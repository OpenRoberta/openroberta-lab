package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class DrehSensor extends Sensor {
    private final Mode mode;
    private final String motor;

    private DrehSensor(Mode mode, String motor) {
        super(Phrase.Kind.DrehSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.motor = motor;
        setReadOnly();
    }

    public static DrehSensor make(Mode mode, String motor) {
        return new DrehSensor(mode, motor);
    }

    public Mode getMode() {
        return this.mode;
    }

    public String getMotor() {
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
        ROTATION, DEGREE, GET_MODE, GET_SAMPLE, RESET;
    }

}
