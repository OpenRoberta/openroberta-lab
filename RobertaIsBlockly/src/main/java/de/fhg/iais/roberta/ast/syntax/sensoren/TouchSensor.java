package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class TouchSensor extends Sensor {
    private final int port;

    private TouchSensor(int port) {
        super(Phrase.Kind.TouchSensor);
        this.port = port;
        setReadOnly();
    }

    public static TouchSensor make(int port) {
        return new TouchSensor(port);
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
        return "TouchSensor [ port=" + this.port + "]";
    }

}
