package de.fhg.iais.roberta.syntax.sensor.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

/**
 * This class represents the <b>mbedSensors_ambientLight_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate
 * code for checking the ambient light.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class AmbientLightSensor<V> extends Sensor<V> {

    private AmbientLightSensor(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("AMBIENTLIGHT_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link AmbientLightSensor}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link AmbientLightSensor}
     */
    public static <V> AmbientLightSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AmbientLightSensor<>(properties, comment);
    }

    @Override
    public String toString() {
        return "AmbientLightSensor []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitAmbientLightSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return AmbientLightSensor.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
