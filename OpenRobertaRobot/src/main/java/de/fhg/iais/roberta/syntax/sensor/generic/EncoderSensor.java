package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robSensors_encoder_getMode</b>, <b>robSensors_encoder_getSample</b> and <b>robSensors_encoder_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link ActorPort} and {@link MotorTachoMode}. See enum {@link MotorTachoMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(MotorTachoMode, ActorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class EncoderSensor<V> extends Sensor<V> {
    private final IMotorTachoMode mode;
    private final IActorPort motor;

    private EncoderSensor(IMotorTachoMode mode, IActorPort motor, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ENCODER_SENSING"),properties, comment);
        Assert.isTrue(mode != null && motor != null);
        this.mode = mode;
        this.motor = motor;
        setReadOnly();
    }

    /**
     * Create object of the class {@link EncoderSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link MotorTachoMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link EncoderSensor}
     */
    public static <V> EncoderSensor<V> make(IMotorTachoMode mode, IActorPort motor, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EncoderSensor<V>(mode, motor, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link MotorTachoMode} for all possible modes that the sensor have
     */
    public IMotorTachoMode getMode() {
        return this.mode;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports
     */
    public IActorPort getMotorPort() {
        return this.motor;
    }

    @Override
    public String toString() {
        return "DrehSensor [mode=" + this.mode + ", motor=" + this.motor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitEncoderSensor(this);
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
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_ENCODER_RESET) ) {
            List<Field> fields = helper.extractFields(block, (short) 1);
            String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
            return EncoderSensor
                .make(factory.getMotorTachoMode("RESET"), factory.getActorPort(portName), helper.extractBlockProperties(block), helper.extractComment(block));
        }
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return EncoderSensor
            .make(factory.getMotorTachoMode(modeName), factory.getActorPort(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getMotorPort().toString();
        if ( !getMode().toString().equals("RESET") ) {
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().toString());
        }
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, fieldValue);

        return jaxbDestination;
    }
}
