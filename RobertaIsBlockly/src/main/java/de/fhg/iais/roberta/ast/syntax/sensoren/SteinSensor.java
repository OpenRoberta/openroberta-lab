package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class SteinSensor extends Sensor {
    private final String key;
    private final Mode mode;

    private SteinSensor(Mode mode, String key) {
        super(Phrase.Kind.SteinSensor);
        Assert.isTrue(mode != null && !key.equals(""));
        this.mode = mode;
        this.key = key;
        setReadOnly();
    }

    public static SteinSensor make(Mode mode, String key) {
        return new SteinSensor(mode, key);
    }

    public String getKey() {
        return this.key;
    }

    public Mode getMode() {
        return this.mode;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.key + ")");
    }

    @Override
    public String toString() {
        return "SteinSensor [key=" + this.key + ", mode=" + this.mode + "]";
    }

    public static enum Mode {
        IS_PRESSED, WAIT_FOR_PRESS, WAIT_FOR_PRESS_AND_RELEASE;
    }

}
