package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        String port = Jaxb2Ast.optField(fields, BlocklyConstants.ACTORPORT);
        boolean actorPortAndMode;
        String pinvalue = null;
        if ( port != null ) {
            actorPortAndMode = true;
            pinvalue = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        } else {
            actorPortAndMode = false;
            port = Jaxb2Ast.extractField(fields, BlocklyConstants.PIN);
            pinvalue = Jaxb2Ast.extractField(fields, BlocklyConstants.VALUETYPE);
        }
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        return PinWriteValueAction
            .make(
                factory.getMode(pinvalue),
                Jaxb2Ast.sanitizePort(port),
                Jaxb2Ast.convertPhraseToExpr(value),
                actorPortAndMode,
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        Mutation mutation = new Mutation();
        mutation.setProtocol(this.pinValue.toString());
        jaxbDestination.setMutation(mutation);

        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        Ast2Jaxb.addField(jaxbDestination, this.actorPortAndMode ? BlocklyConstants.ACTORPORT : BlocklyConstants.PIN, this.port);
        Ast2Jaxb.addField(jaxbDestination, this.actorPortAndMode ? BlocklyConstants.MODE : BlocklyConstants.VALUETYPE, this.pinValue);
        return jaxbDestination;
    }
}
