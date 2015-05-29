package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;

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

    private TouchSensor(SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, Phrase.Kind.TOUCH_SENSING, properties, comment);
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
    public static <V> TouchSensor<V> make(SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TouchSensor<V>(port, properties, comment);
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
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        return TouchSensor.make(SensorPort.get(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getPort().getPortNumber();
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }

}
