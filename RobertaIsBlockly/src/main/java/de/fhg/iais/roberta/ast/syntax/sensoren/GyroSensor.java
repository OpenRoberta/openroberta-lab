package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.aktion.SensorPort;
import de.fhg.iais.roberta.dbc.Assert;

public class GyroSensor extends Sensor {
    private final Mode mode;
    private final SensorPort port;

    private GyroSensor(Mode mode, SensorPort port) {
        super(Phrase.Kind.GyroSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    public static GyroSensor make(Mode mode, SensorPort port) {
        return new GyroSensor(mode, port);
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
        return "GyroSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    public static enum Mode {
        RATE, ANGLE, GET_MODE, GET_SAMPLE, RESET;
    }

}
