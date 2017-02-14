package de.fhg.iais.roberta.syntax.sensor.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.mbed.MbedPins;
import de.fhg.iais.roberta.mode.sensor.mbed.ValueType;
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
 * This class represents the <b>mbedSensors_pin_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate code for reading values from a given pin.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PinGetValueSensor<V> extends Sensor<V> {
    private final ValueType valueType;
    private final MbedPins pin;

    private PinGetValueSensor(MbedPins pin, ValueType valueType, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PIN_VALUE"), properties, comment);
        Assert.notNull(pin);
        Assert.notNull(valueType);
        this.pin = pin;
        this.valueType = valueType;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinGetValueSensor}.
     *
     * @param pin
     * @param valueType see {@link ValueType}
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinGetValueSensor}
     */
    public static <V> PinGetValueSensor<V> make(MbedPins pin, ValueType valueType, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinGetValueSensor<V>(pin, valueType, properties, comment);
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public MbedPins getPin() {
        return this.pin;
    }

    @Override
    public String toString() {
        return "PinValueSensor [" + this.pin.getPinNumber() + ", " + this.valueType + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitPinGetValueSensor(this);

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
        String pinNumber = helper.extractField(fields, BlocklyConstants.PIN);
        String valueType = helper.extractField(fields, BlocklyConstants.VALUETYPE);
        return PinGetValueSensor
            .make(MbedPins.findPin(pinNumber), ValueType.get(valueType), helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VALUETYPE, this.valueType.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PIN, String.valueOf(this.pin.getPinNumber()));
        return jaxbDestination;
    }
}
