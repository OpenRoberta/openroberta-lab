package de.fhg.iais.roberta.syntax.sensor.bob3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.Bob3AstVisitor;

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
public class Bob3TouchSensor<V> extends Sensor<V> {
    private final ITouchSensorMode mode;
    private final String arm_side;
    private final String arm_part;

    private Bob3TouchSensor(String arm_side, String arm_part, ITouchSensorMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOB3_ARMS"), properties, comment);
        this.mode = mode;
        this.arm_part = arm_part;
        this.arm_side = arm_side;
        setReadOnly();
    }

    /**
     * Create object of the class {@link Bob3TouchSensor}.
     *
     * @param port on which the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible ports that the sensor can be
     *        connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link Bob3TouchSensor}
     */
    public static <V> Bob3TouchSensor<V> make(
        String arm_side,
        String arm_part,
        ITouchSensorMode mode,
        ISensorPort port,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new Bob3TouchSensor<V>(arm_side, arm_part, mode, port, properties, comment);
    }

    /**
     * @return the mode
     */
    public ITouchSensorMode getMode() {
        return this.mode;
    }

    public String getArmSide() {
        return this.arm_side;
    }

    public String getArmPart() {
        return this.arm_part;
    }

    @Override
    public String toString() {
        return "TouchSensor []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((Bob3AstVisitor<V>) visitor).visitTouchSensor(this);
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
        String arm_side = helper.extractField(fields, BlocklyConstants.ARMSIDE);
        String arm_part = helper.extractField(fields, BlocklyConstants.ARMPART);
        return Bob3TouchSensor.make(
            arm_side,
            arm_part,
            factory.getTouchSensorMode("UPPER"),
            factory.getSensorPort(arm_side),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ARMSIDE, getArmSide());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ARMPART, getArmPart());
        return jaxbDestination;
    }
}
