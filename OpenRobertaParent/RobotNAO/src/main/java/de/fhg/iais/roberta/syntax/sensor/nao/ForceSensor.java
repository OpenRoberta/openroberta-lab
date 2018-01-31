package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from
 * this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class ForceSensor<V> extends Sensor<V> {

    private final Side side;

    private ForceSensor(Side side, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("FORCESENSOR"), properties, comment);
        Assert.notNull(side, "Missing side in force sensor block!");
        this.side = side;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ForceSensor}. This instance is read only and can not be modified.
     *
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ForceSensor}
     */
    static <V> ForceSensor<V> make(Side side, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ForceSensor<>(side, properties, comment);
    }

    public Side getSide() {
        return this.side;
    }

    @Override
    public String toString() {
        return "ForceSensor [" + this.side + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitForceSensor(this);
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

        String side = helper.extractField(fields, BlocklyConstants.SIDE);

        return ForceSensor.make(Side.valueOf(side), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, this.side.toString());

        return jaxbDestination;
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum Side {
        LEFT( "left" ), RIGHT( "right" );

        private final String pythonCode;

        private Side(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getPythonCode() {
            return "'" + this.pythonCode + "'";
        }
    }
}
