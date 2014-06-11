package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class InfraredSensor extends Sensor {
    private final Mode mode;
    private final int port;

    private InfraredSensor(Mode mode, int port) {
        super(Phrase.Kind.InfraredSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    public static InfraredSensor make(Mode mode, int port) {
        return new InfraredSensor(mode, port);
    }

    public Mode getMode() {
        return this.mode;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "InfraredSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    public static enum Mode {
        DISTANCE, SEEK, GET_MODE, GET_SAMPLE;
    }

}
