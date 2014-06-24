package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class TimerSensor extends Sensor {
    private final Mode mode;
    private final int timer;

    private TimerSensor(Mode mode, int timer) {
        super(Phrase.Kind.SteinSensor);
        Assert.isTrue(timer < 10);
        this.mode = mode;
        this.timer = timer;
        setReadOnly();
    }

    public static TimerSensor make(Mode mode, int timer) {
        return new TimerSensor(mode, timer);
    }

    public Mode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "TimerSensor [mode=" + this.mode + ", timer=" + this.timer + "]";
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.timer + ")");
    }

    public static enum Mode {
        RESET, GET_SAMPLE;
    }
}
