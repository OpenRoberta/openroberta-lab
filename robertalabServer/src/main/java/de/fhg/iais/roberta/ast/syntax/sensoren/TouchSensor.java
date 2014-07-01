package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * Touch sensor, client has to provide the correct port of the sensor.
 * 
 * @author kcvejoski
 */
public class TouchSensor extends Sensor {
    private final SensorPort port;

    private TouchSensor(SensorPort port) {
        super(Phrase.Kind.TouchSensor);
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TouchSensor}.
     * 
     * @param port on which the sensor is connected. See enum {@link SensorPort} for all possible ports that the sensor can be connected.
     * @return
     */
    public static TouchSensor make(SensorPort port) {
        return new TouchSensor(port);
    }

    /**
     * @return port on which
     */
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
