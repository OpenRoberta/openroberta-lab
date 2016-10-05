package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;

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
public class TouchSensor<V> extends BaseSensor<V> {
    private final ITouchSensorMode mode;

    private TouchSensor(ITouchSensorMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("TOUCH_SENSING"), properties, comment);
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TouchSensor}.
     *
     * @param port on which the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible ports that the sensor can be
     *        connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link TouchSensor}
     */
    public static <V> TouchSensor<V> make(ITouchSensorMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TouchSensor<V>(mode, port, properties, comment);
    }

    /**
     * @return the mode
     */
    public ITouchSensorMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "TouchSensor [port=" + getPort() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitTouchSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        return TouchSensor
            .make(factory.getTouchSensorMode("TOUCH"), factory.getSensorPort(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getPort().getPortNumber();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }
}
