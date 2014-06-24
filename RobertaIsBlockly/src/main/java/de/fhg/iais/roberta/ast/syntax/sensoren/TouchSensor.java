package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class TouchSensor extends Sensor {
    private final SensorPort port;

    private TouchSensor(SensorPort port) {
        super(Phrase.Kind.TouchSensor);
        this.port = port;
        setReadOnly();
    }

    public static TouchSensor make(SensorPort port) {
        return new TouchSensor(port);
    }

    public SensorPort getPort() {
        return this.port;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        sb.append("(" + this.port + ")");

    }

    @Override
    public String toString() {
        return "TouchSensor [port=" + this.port + "]";
    }

}
