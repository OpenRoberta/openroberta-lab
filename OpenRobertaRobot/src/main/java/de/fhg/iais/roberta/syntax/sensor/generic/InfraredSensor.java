package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

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
    private final IInfraredSensorMode mode;

    private InfraredSensor(IInfraredSensorMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("INFRARED_SENSING"), properties, comment);
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
    static <V> InfraredSensor<V> make(IInfraredSensorMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new InfraredSensor<V>(mode, port, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link InfraredSensorMode} for all possible modes that the sensor have.
     */
    public IInfraredSensorMode getMode() {
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
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return InfraredSensor
            .make(factory.getInfraredSensorMode(modeName), factory.getSensorPort(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setMode(getMode().toString());
        jaxbDestination.setMutation(mutation);
        String fieldValue = getPort().getPortNumber();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }
}
