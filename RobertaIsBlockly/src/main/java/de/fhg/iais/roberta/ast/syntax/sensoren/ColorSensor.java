package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class ColorSensor extends Sensor {
    private final Mode mode;
    private final int port;

    private ColorSensor(Mode mode, int port) {
        super(Phrase.Kind.ColorSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    public static ColorSensor make(Mode mode, int port) {
        return new ColorSensor(mode, port);
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
        return "ColorSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    public static enum Mode {
        COLOUR, LIGHT, AMBIENTLIGHT, GET_MODE, GET_SAMPLE;
    }
}
