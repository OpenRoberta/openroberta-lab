package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
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
 * This class represents the <b>robSensors_key_isPressed</b> and <b>robSensors_key_isPressedAndReleased</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if a button on the brick is pressed.<br/>
 * <br>
 * The client must provide the {@link BrickKey} and {@link Mode}. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, BrickKey, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class BrickSensor<V> extends Sensor<V> {
    private final IBrickKey key;
    private final Mode mode;

    private BrickSensor(Mode mode, IBrickKey key, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BRICK_SENSING"),properties, comment);
        Assert.isTrue(mode != null && key != null);
        this.mode = mode;
        this.key = key;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BrickSensor}. This instance is read only and can not be modified.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link Mode} for all possible modes that the sensor have
     * @param key on the brick; must be <b>not</b> null; see enum {@link BrickKey} for all possible keys,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BrickSensor}
     */
    public static <V> BrickSensor<V> make(Mode mode, IBrickKey key, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BrickSensor<V>(mode, key, properties, comment);
    }

    /**
     * @return get the key. See enum {@link BrickKey} for all possible keys
     */
    public IBrickKey getKey() {
        return this.key;
    }

    /**
     * @return get the mode of sensor. See enum {@link Mode} for all possible modes that the sensor have
     */
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "BrickSensor [key=" + this.key + ", mode=" + this.mode + "]";
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum Mode {
        IS_PRESSED, WAIT_FOR_PRESS, WAIT_FOR_PRESS_AND_RELEASE;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBrickSensor(this);
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
        String portName = helper.extractField(fields, BlocklyConstants.KEY);
        return BrickSensor.make(BrickSensor.Mode.IS_PRESSED, factory.getBrickKey(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getKey().toString();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.KEY, fieldValue);

        return jaxbDestination;
    }
}
