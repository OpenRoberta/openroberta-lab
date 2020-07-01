package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;

/**
 * This class represents the <b>mbedActions_write_to_pin</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for reading values from a given pin.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PinWriteValueAction<V> extends Action<V> {
    private final String pinValue;
    private final String port;
    private final Expr<V> value;
    private final boolean actorPortAndMode; // true: arduino (uses actor port and mode); if false: calliope (uses pin and valueType :-)

    private PinWriteValueAction(
        String pinValue,
        String port,
        Expr<V> value,
        boolean actorPortAndMode,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PIN_WRITE_VALUE"), properties, comment);
        Assert.notNull(pinValue);
        Assert.notNull(port);
        Assert.notNull(value);
        this.pinValue = pinValue;
        this.port = port;
        this.value = value;
        this.actorPortAndMode = actorPortAndMode;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinWriteValueAction}.
     *
     * @param pin
     * @param valueType see {@link PinValue}
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinWriteValueAction}
     */
    public static <V> PinWriteValueAction<V> make(
        String pinValue,
        String port,
        Expr<V> value,
        boolean actorPortAndMode,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new PinWriteValueAction<>(pinValue, port, value, actorPortAndMode, properties, comment);
    }

    public String getMode() {
        return this.pinValue;
    }

    public String getPort() {
        return this.port;
    }

    public Expr<V> getValue() {
        return this.value;
    }

    public boolean isActorPortAndMode() {
        return this.actorPortAndMode;
    }

    @Override
    public String toString() {
        return "PinWriteValueAction [" + this.pinValue + ", " + this.port + ", " + this.value + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IPinVisitor<V>) visitor).visitPinWriteValueAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 2);
        List<Value> values = helper.extractValues(block, (short) 1);
        String port = helper.optField(fields, BlocklyConstants.ACTORPORT);
        boolean actorPortAndMode;
        String pinvalue = null;
        if ( port != null ) {
            actorPortAndMode = true;
            pinvalue = helper.extractField(fields, BlocklyConstants.MODE);
        } else {
            actorPortAndMode = false;
            port = helper.extractField(fields, BlocklyConstants.PIN);
            pinvalue = helper.extractField(fields, BlocklyConstants.VALUETYPE);
        }
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        return PinWriteValueAction
            .make(
                factory.getMode(pinvalue),
                factory.sanitizePort(port),
                helper.convertPhraseToExpr(value),
                actorPortAndMode,
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        Mutation mutation = new Mutation();
        mutation.setProtocol(this.pinValue.toString());
        jaxbDestination.setMutation(mutation);

        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        Ast2JaxbHelper.addField(jaxbDestination, this.actorPortAndMode ? BlocklyConstants.ACTORPORT : BlocklyConstants.PIN, this.port);
        Ast2JaxbHelper.addField(jaxbDestination, this.actorPortAndMode ? BlocklyConstants.MODE : BlocklyConstants.VALUETYPE, this.pinValue);
        return jaxbDestination;
    }
}
