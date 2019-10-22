package de.fhg.iais.roberta.syntax.sensor.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedSensors_getRssi</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * getting the strength of the last package.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class RadioRssiSensor<V> extends Sensor<V> {

    private RadioRssiSensor(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RADIO_RSSI"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link RadioRssiSensor}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link RadioRssiSensor}
     */
    public static <V> RadioRssiSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RadioRssiSensor<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "RadioRssiSensor []";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitRadioRssiSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return RadioRssiSensor.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
