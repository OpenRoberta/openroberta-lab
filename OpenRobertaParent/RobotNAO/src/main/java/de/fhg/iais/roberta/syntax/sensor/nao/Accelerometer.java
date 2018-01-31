package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
 * The client must provide {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class Accelerometer<V> extends Sensor<V> {

    private final Coordinate coordinate;

    private Accelerometer(Coordinate coordinate, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("Accelerometer"), properties, comment);
        Assert.notNull(coordinate, "Missing coordinate in accelerometer block!");
        this.coordinate = coordinate;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Accelerometer}. This instance is read only and can not be modified.
     *
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Accelerometer}
     */
    static <V> Accelerometer<V> make(Coordinate coordinate, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Accelerometer<>(coordinate, properties, comment);
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    @Override
    public String toString() {
        return "Accelerometer [" + this.coordinate + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitAccelerometer(this);
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

        String coordinate = helper.extractField(fields, BlocklyConstants.COORDINATE);

        return Accelerometer.make(Coordinate.valueOf(coordinate), helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COORDINATE, this.coordinate.toString());

        return jaxbDestination;
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum Coordinate {
        X( "x" ), Y( "y" ), Z( "z" );

        private final String pythonCode;

        private Coordinate(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getPythonCode() {
            return "'" + this.pythonCode + "'";
        }
    }
}
