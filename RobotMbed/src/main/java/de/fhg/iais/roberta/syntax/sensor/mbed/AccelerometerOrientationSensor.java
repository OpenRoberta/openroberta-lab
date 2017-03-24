package de.fhg.iais.roberta.syntax.sensor.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
 * This class represents the <b>mbedSensors_rotation_getSample</b> block from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate code for sampling the orientation.<br/>
 * <br>
 * The client must provide the mode (PITCH or ROLL) <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class AccelerometerOrientationSensor<V> extends Sensor<V> {
    private final Mode mode;

    private AccelerometerOrientationSensor(Mode direction, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ACCELERATION_ORIENTATION_SENSING"), properties, comment);
        Assert.notNull(direction);
        this.mode = direction;
        setReadOnly();
    }

    /**
     * Creates instance of {@link AccelerometerOrientationSensor}. This instance is read only and can not be modified.
     *
     * @param mode
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link AccelerometerOrientationSensor}
     */
    public static <V> AccelerometerOrientationSensor<V> make(Mode direction, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AccelerometerOrientationSensor<V>(direction, properties, comment);
    }

    /**
     * @return get the direction.
     */
    public Mode getAccelerationOrientationMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "AccelerometerOrientationSensor [" + this.mode + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitAccelerometerOrientationSensor(this);
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
        String direction = helper.extractField(fields, BlocklyConstants.MODE);
        return AccelerometerOrientationSensor.make(Mode.valueOf(direction), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE, String.valueOf(this.mode));

        return jaxbDestination;
    }

    /**
     * Modes in which the sensor can operate.
     */

    public static enum Mode {
        PITCH( "getPitch()" ), ROLL( "getRoll()" );

        private final String cppCode;

        private Mode(String cppCode) {
            this.cppCode = cppCode;
        }

        public String getCppCode() {
            return this.cppCode;
        }
    }

}
