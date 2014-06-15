package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.aktion.SensorPort;
import de.fhg.iais.roberta.dbc.Assert;

public class InfraredSensor extends Sensor {
    private final Mode mode;
    private final SensorPort port;

    private InfraredSensor(Mode mode, SensorPort port) {
        super(Phrase.Kind.InfraredSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    public static InfraredSensor make(Mode mode, SensorPort port) {
        return new InfraredSensor(mode, port);
    }

    public Mode getMode() {
        return this.mode;
    }

    public SensorPort getPort() {
        return this.port;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.port + ")");
    }

    @Override
    public String toString() {
        return "InfraredSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    public static enum Mode {
        DISTANCE, SEEK, GET_MODE, GET_SAMPLE;
    }

}
