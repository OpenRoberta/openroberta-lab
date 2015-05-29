package de.fhg.iais.roberta.syntax.sensor.ev3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.shared.sensor.ev3.ColorSensorMode;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.BlockType;
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
 * This class represents the <b>robSensors_colour_getMode</b>, <b>robSensors_colour_getSample</b> and <b>robSensors_colour_setMode</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link ColorSensorMode}. See enum {@link ColorSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ColorSensor<V> extends BaseSensor<V> {
    private final ColorSensorMode mode;

    private ColorSensor(ColorSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockType.COLOR_SENSING, properties, comment);
        Assert.isTrue(mode != null && port != null);
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Create object of the class {@link ColorSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link ColorSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ColorSensor}
     */
    static <V> ColorSensor<V> make(ColorSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorSensor<V>(mode, port, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link ColorSensorMode} for all possible modes that the sensor have
     */
    public ColorSensorMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "ColorSensor [mode=" + this.mode + ", port=" + this.getPort() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitColorSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return ColorSensor.make(ColorSensorMode.get(modeName), SensorPort.get(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setMode(getMode().name());
        jaxbDestination.setMutation(mutation);
        String fieldValue = getPort().getPortNumber();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }
}
