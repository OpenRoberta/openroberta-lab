package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class CompassSensor<V> extends Sensor<V> {

    private CompassSensor(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("COMPASS_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link CompassSensor}.
     *
     * @param port on which the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible ports that the sensor can be
     *        connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link CompassSensor}
     */
    public static <V> CompassSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new CompassSensor<>(properties, comment);
    }

    /**
     * @return the mode
     */

    @Override
    public String toString() {
        return "CompassSensor []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitCompassSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return CompassSensor.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
