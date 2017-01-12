package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.sensor.mbed.ValueType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

/**
 * This class represents the <b>mbedActions_write_to_pin</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate code for reading values from a given pin.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PinWriteValueSensor<V> extends Sensor<V> {
    private final ValueType valueType;
    private final int pinNumber;
    private final Expr<V> value;

    private PinWriteValueSensor(int pinNumber, ValueType valueType, Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PIN_WRITE_VALUE"), properties, comment);
        Assert.notNull(value);
        Assert.isTrue(pinNumber >= 0 && pinNumber <= 3 && value.isReadOnly());
        Assert.notNull(valueType);
        this.pinNumber = pinNumber;
        this.valueType = valueType;
        this.value = value;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinWriteValueSensor}.
     *
     * @param pinNumber
     * @param valueType see {@link ValueType}
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinWriteValueSensor}
     */
    public static <V> PinWriteValueSensor<V> make(
        int pinNumber,
        ValueType valueType,
        Expr<V> value,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new PinWriteValueSensor<V>(pinNumber, valueType, value, properties, comment);
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public int getPinNumber() {
        return this.pinNumber;
    }

    public Expr<V> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "PinWriteValueSensor [" + this.pinNumber + ", " + this.valueType + ", " + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitPinWriteValueSensor(this);
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
        List<Value> values = helper.extractValues(block, (short) 1);

        String pinNumber = helper.extractField(fields, BlocklyConstants.PIN);
        String valueType = helper.extractField(fields, BlocklyConstants.VALUETYPE);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, int.class));
        return PinWriteValueSensor.make(
            Integer.valueOf(pinNumber),
            ValueType.get(valueType),
            helper.convertPhraseToExpr(value),
            helper.extractBlockProperties(block),
            helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VALUETYPE, this.valueType.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PIN, String.valueOf(this.pinNumber));
        return jaxbDestination;
    }
}
