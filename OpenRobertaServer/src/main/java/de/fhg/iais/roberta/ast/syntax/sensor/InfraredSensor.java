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
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_infrared_getMode</b>, <b>robSensors_infrared_getSample</b> and <b>robSensors_infrared_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link InfraredSensorMode}. See enum {@link InfraredSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(InfraredSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class InfraredSensor<V> extends BaseSensor<V> {
    private final InfraredSensorMode mode;

    private InfraredSensor(InfraredSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, Phrase.Kind.INFRARED_SENSING, properties, comment);
        Assert.isTrue(mode != null && port != null);
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Create object of the class {@link InfraredSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link InfraredSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link InfraredSensor}
     */
    static <V> InfraredSensor<V> make(InfraredSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new InfraredSensor<V>(mode, port, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link InfraredSensorMode} for all possible modes that the sensor have.
     */
    public InfraredSensorMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "InfraredSensor [mode=" + this.mode + ", port=" + getPort() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitInfraredSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return InfraredSensor.make(
            InfraredSensorMode.get(modeName),
            SensorPort.get(portName),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setMode(getMode().name());
        jaxbDestination.setMutation(mutation);
        String fieldValue = getPort().getPortNumber();
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }
}
