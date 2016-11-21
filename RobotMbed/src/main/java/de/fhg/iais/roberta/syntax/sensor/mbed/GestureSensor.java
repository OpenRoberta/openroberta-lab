package de.fhg.iais.roberta.syntax.sensor.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.sensor.mbed.BrickKey;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

/**
 * This class represents the <b>robSensors_key_isPressed</b> and <b>robSensors_key_isPressedAndReleased</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if a button on the brick is pressed.<br/>
 * <br>
 * The client must provide the {@link BrickKey} and {@link Mode}. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, BrickKey, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class GestureSensor<V> extends Sensor<V> {
    private final GestureMode gestureMode;

    private GestureSensor(GestureMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("GESTURE_SENSING"), properties, comment);
        Assert.notNull(mode);
        this.gestureMode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link GestureSensor}. This instance is read only and can not be modified.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link Mode} for all possible modes that the sensor have
     * @param key on the brick; must be <b>not</b> null; see enum {@link BrickKey} for all possible keys,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link GestureSensor}
     */
    public static <V> GestureSensor<V> make(GestureMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GestureSensor<>(mode, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link Mode} for all possible modes that the sensor have
     */
    public GestureMode getMode() {
        return this.gestureMode;
    }

    @Override
    public String toString() {
        return "GestureSensor [ " + this.gestureMode + " ]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitGestureSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String gestureMode = helper.extractField(fields, BlocklyConstants.GESTURE);
        return GestureSensor.make(GestureMode.valueOf(gestureMode), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.GESTURE, this.gestureMode.toString());

        return jaxbDestination;
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum GestureMode {
        UP( "up" ),
        DOWN( "down" ),
        LEFT( "left" ),
        RIGHT( "righ" ),
        FACE_DOWN( "face down" ),
        FACE_UP( "face up" ),
        SHAKE( "shake" ),
        FREEFALL( "freefall" ),
        G3( "3g" ),
        G6( "6g" ),
        G8( "8g" );

        private final String pythonCode;

        private GestureMode(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getPythonCode() {
            return this.pythonCode;
        }
    }

}
