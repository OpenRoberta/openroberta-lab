package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

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
    private final BrickKey key;
    private final Mode mode;

    private BrickSensor(Mode mode, BrickKey key, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.BRICK_SENSIG, properties, comment);
        Assert.isTrue(mode != null && !key.equals(""));
        this.mode = mode;
        this.key = key;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BrickSensor}. This instance is read only and can not be modified.
     *
     * @param mode in which the sensor is operating. See enum {@link Mode} for all possible modes that the sensor have.
     * @param key on the brick. See enum {@link BrickKey} for all possible keys,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BrickSensor}
     */
    public static <V> BrickSensor<V> make(Mode mode, BrickKey key, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BrickSensor<V>(mode, key, properties, comment);
    }

    /**
     * @return get the key. See enum {@link BrickKey} for all possible keys.
     */
    public BrickKey getKey() {
        return this.key;
    }

    /**
     * @return get the mode of sensor. See enum {@link Mode} for all possible modes that the sensor have.
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

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getKey().name();
        AstJaxbTransformerHelper.addField(jaxbDestination, "KEY", fieldValue);

        return jaxbDestination;
    }
}
