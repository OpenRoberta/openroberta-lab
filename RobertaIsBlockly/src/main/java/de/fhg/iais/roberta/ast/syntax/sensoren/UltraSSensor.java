package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.aktion.SensorPort;
import de.fhg.iais.roberta.dbc.Assert;

public class UltraSSensor extends Sensor {
    private final Mode mode;
    private final SensorPort port;

    private UltraSSensor(Mode mode, SensorPort port) {
        super(Phrase.Kind.UltraSSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    public static UltraSSensor make(Mode mode, SensorPort port) {
        return new UltraSSensor(mode, port);
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
        return "UltraSSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    public static enum Mode {
        DISTANCE, PRESENCE, GET_MODE, GET_SAMPLE;
    }

}
