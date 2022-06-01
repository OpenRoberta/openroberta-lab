package de.fhg.iais.roberta.syntax.lang.methods;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robProcedures_ifreturn</b> block from Blockly into the AST (abstract syntax tree). Object from this class is used to create a
 * <i>if-return</i> statement inside method<br/>
 */
public class MethodIfReturn<V> extends Method<V> {
    private final Expr<V> oraCondition;
    private final BlocklyType oraReturnType;
    private final Expr<V> oraReturnValue;
    private final BigInteger value;

    private MethodIfReturn(
        Expr<V> oraCondition,
        BlocklyType oraReturnType,
        Expr<V> oraReturnValue,
        BigInteger value,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_IF_RETURN"), properties, comment);
        Assert.isTrue(oraCondition != null && oraCondition.isReadOnly() && oraReturnType != null && oraReturnValue != null && oraReturnValue.isReadOnly());
        this.oraCondition = oraCondition;
        this.oraReturnType = oraReturnType;
        this.oraReturnValue = oraReturnValue;
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link MethodIfReturn}. This instance is read only and cannot be modified.
     *
     * @param condition expression, must be <b>not</b> null and read only
     * @param returnType, see {@link BlocklyType} for all possible types
     * @param returnValue
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MethodIfReturn}
     */
    public static <V> MethodIfReturn<V> make(
        Expr<V> condition,
        BlocklyType returnType,
        Expr<V> returnValue,
        BigInteger value,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new MethodIfReturn<>(condition, returnType, returnValue, value, properties, comment);
    }

    public Expr<V> getCondition() {
        return this.oraCondition;
    }

    @Override
    public BlocklyType getReturnType() {
        return this.oraReturnType;
    }

    public Expr<V> getReturnValue() {
        return this.oraReturnValue;
    }


    public BigInteger getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "MethodIfReturn [" + this.oraCondition + ", " + this.oraReturnType + ", " + this.oraReturnValue + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.CONDITION, BlocklyType.BOOLEAN));
        Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NULL));
        final Mutation mutation = block.getMutation();
        String mode = mutation.getReturnType() == null ? "void" : mutation.getReturnType();
        return MethodIfReturn
            .make(
                Jaxb2Ast.convertPhraseToExpr(left),
                BlocklyType.get(mode),
                Jaxb2Ast.convertPhraseToExpr(right),
                mutation.getValue(),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setValue(this.getValue());
        if (!this.oraReturnType.equals(BlocklyType.VOID)) {
            mutation.setReturnType(this.oraReturnType.getBlocklyName());
        }
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.CONDITION, getCondition());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, getReturnValue());

        return jaxbDestination;
    }
}
