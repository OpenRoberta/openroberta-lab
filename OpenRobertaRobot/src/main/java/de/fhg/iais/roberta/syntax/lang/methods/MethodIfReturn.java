package de.fhg.iais.roberta.syntax.lang.methods;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>robProcedures_ifreturn</b> block from Blockly into the AST (abstract syntax tree). Object from this class is used to create a
 * <i>if-return</i> statement inside method<br/>
 */
public class MethodIfReturn<V> extends Method<V> {
    private final Expr<V> oraCondition;
    private final BlocklyType oraReturnType;
    private final Expr<V> oraReturnValue;

    private MethodIfReturn(Expr<V> oraCondition, BlocklyType oraReturnType, Expr<V> oraReturnValue, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_IF_RETURN"), properties, comment);
        Assert.isTrue(oraCondition != null && oraCondition.isReadOnly() && oraReturnType != null && oraReturnValue != null && oraReturnValue.isReadOnly());
        this.oraCondition = oraCondition;
        this.oraReturnType = oraReturnType;
        this.oraReturnValue = oraReturnValue;
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
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new MethodIfReturn<>(condition, returnType, returnValue, properties, comment);
    }

    /**
     * @return the condition
     */
    public Expr<V> getCondition() {
        return this.oraCondition;
    }

    /**
     * @return the returnType
     */
    @Override
    public BlocklyType getReturnType() {
        return this.oraReturnType;
    }

    /**
     * @return the returnValue
     */
    public Expr<V> getReturnValue() {
        return this.oraReturnValue;
    }

    @Override
    public String toString() {
        return "MethodIfReturn [" + this.oraCondition + ", " + this.oraReturnType + ", " + this.oraReturnValue + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitMethodIfReturn(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 2);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.CONDITION, BlocklyType.BOOLEAN));
        Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NULL));
        String mode = block.getMutation().getReturnType() == null ? "void" : block.getMutation().getReturnType();
        return MethodIfReturn
            .make(
                helper.convertPhraseToExpr(left),
                BlocklyType.get(mode),
                helper.convertPhraseToExpr(right),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setValue(BigInteger.ONE);
        mutation.setReturnType(this.oraReturnType.getBlocklyName());
        jaxbDestination.setMutation(mutation);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.CONDITION, getCondition());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getReturnValue());

        return jaxbDestination;
    }
}
