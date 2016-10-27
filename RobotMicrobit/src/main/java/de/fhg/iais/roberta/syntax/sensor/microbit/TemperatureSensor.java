package de.fhg.iais.roberta.syntax.sensor.microbit;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MicrobitAstVisitor;

/**
 * This class represents the <b>mbedSensors_temperature_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate
 * code for checking if the sensor is pressed.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class TemperatureSensor<V> extends Sensor<V> {

    private TemperatureSensor(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TEMP_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link TemperatureSensor}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link TemperatureSensor}
     */
    public static <V> TemperatureSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TemperatureSensor<>(properties, comment);
    }

    @Override
    public String toString() {
        return "TemperatureSensor []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MicrobitAstVisitor<V>) visitor).visitTemperatureSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return TemperatureSensor.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
