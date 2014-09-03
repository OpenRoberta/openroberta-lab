package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

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
public class TouchSensor<V> extends Sensor<V> {
    private final SensorPort port;

    private TouchSensor(SensorPort port, boolean disabled, String comment) {
        super(Phrase.Kind.TOUCH_SENSING, disabled, comment);
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TouchSensor}.
     * 
     * @param port on which the sensor is connected. See enum {@link SensorPort} for all possible ports that the sensor can be connected
     * @param disabled state of the block,
     * @param comment added from the user
     * @return read only object of {@link TouchSensor}
     */
    public static <V> TouchSensor<V> make(SensorPort port, boolean disabled, String comment) {
        return new TouchSensor<V>(port, disabled, comment);
    }

    /**
     * @return port on which
     */
    public SensorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "TouchSensor [port=" + this.port + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitTouchSensor(this);
    }

}
