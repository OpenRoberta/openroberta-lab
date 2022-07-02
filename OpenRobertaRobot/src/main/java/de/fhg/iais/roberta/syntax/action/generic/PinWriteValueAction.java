package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "PIN_WRITE_VALUE", category = "ACTOR", blocklyNames = {"robActions_write_pin", "mbedActions_write_to_pin"})
public final class PinWriteValueAction<V> extends Action<V> {
    public final String pinValue;
    public final String port;
    public final Expr<V> value;
    public final boolean actorPortAndMode; // true: arduino (uses actor port and mode); if false: calliope (uses pin and valueType :-)

    public PinWriteValueAction(
        String pinValue,
        String port,
        Expr<V> value,
        boolean actorPortAndMode,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(properties, comment);
        Assert.notNull(pinValue);
        Assert.notNull(port);
        Assert.notNull(value);
        this.pinValue = pinValue;
        this.port = port;
        this.value = value;
        this.actorPortAndMode = actorPortAndMode;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "PinWriteValueAction [" + this.pinValue + ", " + this.port + ", " + this.value + "]";
    }

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
        return new PinWriteValueAction<>(factory.getMode(pinvalue), Jaxb2Ast.sanitizePort(port), Jaxb2Ast.convertPhraseToExpr(value), actorPortAndMode, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
