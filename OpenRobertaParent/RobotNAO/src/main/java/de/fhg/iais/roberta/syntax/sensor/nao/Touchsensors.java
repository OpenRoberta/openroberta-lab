package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Posture;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_touchsensor</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * reading the status of the touchsensors<br/>
 * <br/>
 * The client must provide the {@link Posture} (name of posture).
 */
public final class Touchsensors<V> extends de.fhg.iais.roberta.syntax.sensor.Sensor<V> {

    private final SensorType sensor;
    private final TouchSide side;

    private Touchsensors(SensorType sensor, TouchSide side, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TOUCHSENSORS"), properties, comment);
        Assert.notNull(sensor, "Missing sensor in Touchsensors block!");
        Assert.notNull(side, "Missing side in Touchsensors block!");
        this.sensor = sensor;
        this.side = side;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Touchsensors}. This instance is read only and can not be modified.
     *
     * @param port {@link Posture} which will be applied,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Touchsensors}
     */
    static <V> Touchsensors<V> make(SensorType sensor, TouchSide side, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Touchsensors<V>(sensor, side, properties, comment);
    }

    public TouchSide getSide() {
        return this.side;
    }

    @Override
    public String toString() {
        return "Touchsensors [" + this.sensor + ", " + this.side + "]";
    }

    public SensorType getSensor() {
        return this.sensor;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitTouchsensors(this);
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

        String sensor = helper.extractField(fields, BlocklyConstants.POSITION);
        String side = helper.extractField(fields, BlocklyConstants.SIDE);

        return Touchsensors.make(SensorType.valueOf(sensor), TouchSide.valueOf(side), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.POSITION, this.sensor.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, this.side.toString());

        return jaxbDestination;
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum SensorType {
        HAND( "hand" ), HEAD( "head" ), BUMPER( "bumper" );

        private final String pythonCode;

        private SensorType(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getPythonCode() {
            return this.pythonCode;
        }
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum TouchSide {
        LEFT( "left" ), RIGHT( "right" ), FRONT( "front" ), MIDDLE( "middle" ), REAR( "rear" );

        private final String pythonCode;

        private TouchSide(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getPythonCode() {
            return this.pythonCode;
        }
    }
}
