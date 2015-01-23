package de.fhg.iais.roberta.ast.syntax.functions;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>text_join</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class TextJoinFunct<V> extends Function<V> {
    private final List<Expr<V>> param;

    private TextJoinFunct(List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.TEXT_JOIN_FUNCT, properties, comment);
        Assert.isTrue(param != null);
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link TextJoinFunct}. This instance is read only and can not be modified.
     *
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link TextJoinFunct}
     */
    public static <V> TextJoinFunct<V> make(List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TextJoinFunct<V>(param, properties, comment);
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
        return visitor.visitTextJoinFunct(this);
    }

    @Override
    public String toString() {
        return "TextJoinFunct [" + this.param + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        ExprList<?> strExprList = (ExprList<?>) getParam().get(0);
        int numOfStrings = (strExprList.get().size());
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfStrings));
        jaxbDestination.setMutation(mutation);
        for ( int i = 0; i < numOfStrings; i++ ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ADD + i, strExprList.get().get(i));
        }
        return jaxbDestination;
    }
}
