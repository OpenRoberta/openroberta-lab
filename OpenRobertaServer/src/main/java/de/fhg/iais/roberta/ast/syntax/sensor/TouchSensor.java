package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

/**
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class TouchSensor<V> extends Sensor<V> {
    private final SensorPort port;

    private TouchSensor(SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.TOUCH_SENSING, properties, comment);
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TouchSensor}.
     *
     * @param port on which the sensor is connected. See enum {@link SensorPort} for all possible ports that the sensor can be connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link TouchSensor}
     */
    public static <V> TouchSensor<V> make(SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TouchSensor<V>(port, properties, comment);
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

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getPort().getPortNumber();
        AstJaxbTransformerHelper.addField(jaxbDestination, "SENSORPORT", fieldValue);

        return jaxbDestination;
    }

}
