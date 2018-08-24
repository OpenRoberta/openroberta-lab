package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.IPinValue;
import de.fhg.iais.roberta.mode.sensor.PinValue;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.ArduinoAstVisitor;

public class PinWriteValueAction<V> extends Action<V> {
    private final IPinValue pinValue;
    private final IActorPort port;
    private final Expr<V> value;

    private PinWriteValueAction(IPinValue pinValue, IActorPort port, Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WRITE_TO_PIN"), properties, comment);
        Assert.notNull(pinValue);
        Assert.notNull(port);
        Assert.notNull(value);
        this.pinValue = pinValue;
        this.port = port;
        this.value = value;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinWriteValue}.
     *
     * @param pin
     * @param valueType see {@link PinValue}
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinWriteValue}
     */
    public static <V> PinWriteValueAction<V> make(
        IPinValue pinValue,
        IActorPort port,
        Expr<V> value,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new PinWriteValueAction<>(pinValue, port, value, properties, comment);
    }

    public IPinValue getMode() {
        return this.pinValue;
    }

    public IActorPort getPort() {
        return this.port;
    }

    public Expr<V> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "PinWriteValueSensor [" + this.pinValue + ", " + this.port + ", " + this.value + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 2);
        List<Value> values = helper.extractValues(block, (short) 1);
        String port = helper.extractField(fields, BlocklyConstants.PIN);
        String pinvalue = helper.extractField(fields, BlocklyConstants.VALUETYPE);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        return PinWriteValueAction.make(
            factory.getPinGetValueSensorMode(pinvalue),
            factory.getActorPort(port),
            helper.convertPhraseToExpr(value),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation = new Mutation();
        mutation.setProtocol(this.pinValue.toString());
        jaxbDestination.setMutation(mutation);
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VALUETYPE, this.pinValue.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PIN, this.port.getOraName());
        return jaxbDestination;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((ArduinoAstVisitor<V>) visitor).visitPinWriteValueAction(this);
    }

}
