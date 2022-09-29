package de.fhg.iais.roberta.syntax.lang.methods;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robProcedures_ifreturn</b> block from Blockly into the AST (abstract syntax tree). Object from this class is used to create a
 * <i>if-return</i> statement inside method<br/>
 */
@NepoBasic(name = "METHOD_IF_RETURN", category = "METHOD", blocklyNames = {"robProcedures_ifreturn"})
public final class MethodIfReturn extends Method {
    public final Expr oraCondition;
    public final BlocklyType oraReturnType;
    public final Expr oraReturnValue;
    public final BigInteger value;

    public MethodIfReturn(
        Expr oraCondition,
        BlocklyType oraReturnType,
        Expr oraReturnValue,
        BigInteger value,
        BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(oraCondition != null && oraCondition.isReadOnly() && oraReturnType != null && oraReturnValue != null && oraReturnValue.isReadOnly());
        this.oraCondition = oraCondition;
        this.oraReturnType = oraReturnType;
        this.oraReturnValue = oraReturnValue;
        this.value = value;
        setReadOnly();
    }

    @Override
    public BlocklyType getReturnType() {
        return this.oraReturnType;
    }

    public BigInteger getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "MethodIfReturn [" + this.oraCondition + ", " + this.oraReturnType + ", " + this.oraReturnValue + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        Phrase left = helper.extractValue(values, new ExprParam(BlocklyConstants.CONDITION, BlocklyType.BOOLEAN));
        Phrase right = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NULL));
        final Mutation mutation = block.getMutation();
        String mode = mutation.getReturnType() == null ? "void" : mutation.getReturnType();
        return new MethodIfReturn(Jaxb2Ast.convertPhraseToExpr(left), BlocklyType.get(mode), Jaxb2Ast.convertPhraseToExpr(right), mutation.getValue(), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setValue(this.getValue());
        if ( !this.oraReturnType.equals(BlocklyType.VOID) ) {
            mutation.setReturnType(this.oraReturnType.getBlocklyName());
        }
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.CONDITION, this.oraCondition);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.oraReturnValue);

        return jaxbDestination;
    }
}
