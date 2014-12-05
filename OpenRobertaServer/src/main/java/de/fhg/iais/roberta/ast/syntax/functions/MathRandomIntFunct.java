package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>math_random_int</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class MathRandomIntFunct<V> extends Function<V> {
    private final List<Expr<V>> param;

    private MathRandomIntFunct(List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.MATH_RANDOM_INT_FUNCT, properties, comment);
        Assert.isTrue(param != null);
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MathRandomIntFunct}. This instance is read only and can not be modified.
     *
     * @param param list of parameters for the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathRandomIntFunct}
     */
    public static <V> MathRandomIntFunct<V> make(List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathRandomIntFunct<V>(param, properties, comment);
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMathRandomIntFunct(this);
    }

    @Override
    public String toString() {
        return "MathRandomIntFunct [" + this.param + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addValue(jaxbDestination, "FROM", getParam().get(0));
        AstJaxbTransformerHelper.addValue(jaxbDestination, "TO", getParam().get(1));
        return jaxbDestination;
    }
}
