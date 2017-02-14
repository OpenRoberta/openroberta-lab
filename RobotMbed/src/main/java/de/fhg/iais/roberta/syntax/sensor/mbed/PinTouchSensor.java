package de.fhg.iais.roberta.syntax.sensor.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.mbed.MbedPins;
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
 * This class represents the <b>mbedSensors_pin_isTouched</b> block from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if a pin is touched.<br/>
 * <br>
 * The client must provide the pin number <br>
 * <br>
 * To create an instance from this class use the method {@link #make(int, BrickKey, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PinTouchSensor<V> extends Sensor<V> {
    private MbedPins pin;

    private PinTouchSensor(MbedPins pinNumber, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PIN_TOUCH_SENSING"), properties, comment);
        Assert.notNull(pinNumber);
        this.pin = pinNumber;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PinTouchSensor}. This instance is read only and can not be modified.
     *
     * @param pinNumber
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PinTouchSensor}
     */
    public static <V> PinTouchSensor<V> make(MbedPins pinNumber, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinTouchSensor<V>(pinNumber, properties, comment);
    }

    /**
     * @return get the number of pin.
     */
    public MbedPins getPin() {
        return this.pin;
    }

    @Override
    public String toString() {
        return "PinTouchSensor [" + this.pin.getPinNumber() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitPinTouchSensor(this);
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
        String pinNumber = helper.extractField(fields, BlocklyConstants.PIN);
        return PinTouchSensor.make(MbedPins.findPin(pinNumber), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PIN, String.valueOf(this.pin.getPinNumber()));

        return jaxbDestination;
    }

}
