package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort)}.<br>
 */
public class TouchSensor extends Sensor {
    private final SensorPort port;

    private TouchSensor(SensorPort port) {
        super(Phrase.Kind.TOUCH_SENSING);
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TouchSensor}.
     * 
     * @param port on which the sensor is connected. See enum {@link SensorPort} for all possible ports that the sensor can be connected.
     * @return read only object of {@link TouchSensor}
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
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("hal.isPressed(" + this.port + ")");

    }

    @Override
    public String toString() {
        return "TouchSensor [port=" + this.port + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
