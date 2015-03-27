package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>math_trig</b>, <b>math_round</b> and <b>math_single</b> blocks from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(FunctionNames, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class MathSingleFunct<V> extends Function<V> {
    private final FunctionNames functName;
    private final List<Expr<V>> param;

    private MathSingleFunct(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.MATH_SINGLE_FUNCT, properties, comment);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MathSingleFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathSingleFunct}
     */
    public static <V> MathSingleFunct<V> make(FunctionNames name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathSingleFunct<V>(name, param, properties, comment);
    }

    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        if ( helper.getOperation(block, BlocklyConstants.OP_).equals(BlocklyConstants.NEG) ) {
            return helper.blockToUnaryExpr(block, new ExprParam(BlocklyConstants.NUM, Integer.class), BlocklyConstants.OP_);
        }
        List<ExprParam> exprParams = new ArrayList<ExprParam>();
        exprParams.add(new ExprParam(BlocklyConstants.NUM, Integer.class));
        String op = helper.getOperation(block, BlocklyConstants.OP_);
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return MathSingleFunct.make(FunctionNames.get(op), params, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    /**
     * @return name of the function
     */
    public FunctionNames getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMathSingleFunct(this);
    }

    @Override
    public String toString() {
        return "MathSingleFunct [" + this.functName + ", " + this.param + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.OP_, getFunctName().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.NUM, getParam().get(0));
        return jaxbDestination;
    }

}
