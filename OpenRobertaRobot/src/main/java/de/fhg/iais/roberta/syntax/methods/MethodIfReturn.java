package de.fhg.iais.roberta.syntax.methods;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robProcedures_ifreturn</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class is used to create a <i>if-return</i> statement inside method<br/>
 */
public class MethodIfReturn<V> extends Method<V> {
    private final Expr<V> condition;
    private final BlocklyType returnType;
    private final Expr<V> returnValue;

    private MethodIfReturn(Expr<V> condition, BlocklyType returnType, Expr<V> returnValue, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_IF_RETURN"),properties, comment);
        Assert.isTrue(condition != null && condition.isReadOnly() && returnType != null && returnValue != null && returnValue.isReadOnly());
        this.condition = condition;
        this.returnType = returnType;
        this.returnValue = returnValue;
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
        return new MethodIfReturn<V>(condition, returnType, returnValue, properties, comment);
    }

    /**
     * @return the condition
     */
    public Expr<V> getCondition() {
        return this.condition;
    }

    /**
     * @return the returnType
     */
    public BlocklyType getReturnType() {
        return this.returnType;
    }

    /**
     * @return the returnValue
     */
    public Expr<V> getReturnValue() {
        return this.returnValue;
    }

    @Override
    public String toString() {
        return "MethodIfReturn [" + this.condition + ", " + this.returnType + ", " + this.returnValue + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMethodIfReturn(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 2);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.CONDITION, Boolean.class));
        Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, NullConst.class));
        String mode = block.getMutation().getReturnType() == null ? "void" : block.getMutation().getReturnType();
        return MethodIfReturn.make(
            helper.convertPhraseToExpr(left),
            BlocklyType.get(mode),
            helper.convertPhraseToExpr(right),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setValue(BigInteger.ONE);
        mutation.setReturnType(this.returnType.getBlocklyName());
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONDITION, getCondition());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getReturnValue());

        return jaxbDestination;
    }
}
