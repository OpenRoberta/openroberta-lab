package de.fhg.iais.roberta.syntax.lang.functions;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>text_join</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class TextJoinFunct<V> extends Function<V> {
    private final ExprList<V> param;

    private TextJoinFunct(ExprList<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TEXT_JOIN_FUNCT"), properties, comment);
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
    public static <V> TextJoinFunct<V> make(ExprList<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TextJoinFunct<V>(param, properties, comment);
    }

    /**
     * @return list of parameters for the function
     */
    public ExprList<V> getParam() {
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitTextJoinFunct(this);
    }

    @Override
    public String toString() {
        return "TextJoinFunct [" + this.param + "]";
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.STRING;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        ExprList<V> exprList = helper.blockToExprList(block, BlocklyType.STRING);
        return TextJoinFunct.make(exprList, helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        int numOfStrings = getParam().get().size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfStrings));
        jaxbDestination.setMutation(mutation);
        for ( int i = 0; i < numOfStrings; i++ ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ADD + i, getParam().get().get(i));
        }
        return jaxbDestination;
    }
}
