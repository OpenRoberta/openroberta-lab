package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>nnStep</b> block from Blockly in the AST. An object of this class will generate a nnStep statement.<br/>
 */
public class NNStepStmt<V> extends Stmt<V> {

    private final List<Expr<V>> il;
    private final List<Var<V>> ol;

    private NNStepStmt(List<Expr<V>> il, List<Var<V>> ol, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NNSTEP_STMT"), properties, comment);
        this.il = il;
        this.ol = ol;
        setReadOnly();
    }

    /**
     * create <b>nnStep</b> statement.
     *
     * @return read only object of class {@link NNStepStmt}
     */
    public static <V> NNStepStmt<V> make(List<Expr<V>> il, List<Var<V>> ol, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NNStepStmt<>(il, ol, properties, comment);
    }

    public List<Expr<V>> getIl() {
        return il;
    }

    public List<Var<V>> getOl() {
        return ol;
    }

    @Override
    public String toString() {
        return "nnStep()";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitNNStepStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        helper.getDropdownFactory();
        List<Value> values = AbstractJaxb2Ast.extractValues(block, (short) 6);
        Phrase<V> i0 = helper.extractValue(values, new ExprParam("INPUT0", BlocklyType.NUMBER_INT));
        Phrase<V> i1 = helper.extractValue(values, new ExprParam("INPUT1", BlocklyType.NUMBER_INT));
        Phrase<V> i2 = helper.extractValue(values, new ExprParam("INPUT2", BlocklyType.NUMBER_INT));
        List<Expr<V>> il = Arrays.asList(helper.convertPhraseToExpr(i0), helper.convertPhraseToExpr(i1), helper.convertPhraseToExpr(i2));
        Phrase<V> o0 = helper.extractValue(values, new ExprParam("OUTPUT0", BlocklyType.NUMBER_INT));
        Phrase<V> o1 = helper.extractValue(values, new ExprParam("OUTPUT1", BlocklyType.NUMBER_INT));
        Phrase<V> o2 = helper.extractValue(values, new ExprParam("OUTPUT2", BlocklyType.NUMBER_INT));
        Assert.isTrue(o0.getClass().equals(Var.class) && o1.getClass().equals(Var.class) && o2.getClass().equals(Var.class));
        final Var<V> v0 = (Var<V>) helper.convertPhraseToExpr(o0);
        final Var<V> v1 = (Var<V>) helper.convertPhraseToExpr(o1);
        final Var<V> v2 = (Var<V>) helper.convertPhraseToExpr(o2);
        List<Var<V>> ol = Arrays.asList(v0, v1, v2);
        return NNStepStmt.make(il, ol, AbstractJaxb2Ast.extractBlockProperties(block), AbstractJaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.addValue(jaxbDestination, "INPUT0", il.get(0));
        Ast2JaxbHelper.addValue(jaxbDestination, "INPUT1", il.get(1));
        Ast2JaxbHelper.addValue(jaxbDestination, "INPUT2", il.get(2));
        Ast2JaxbHelper.addValue(jaxbDestination, "OUTPUT0", ol.get(0));
        Ast2JaxbHelper.addValue(jaxbDestination, "OUTPUT1", ol.get(1));
        Ast2JaxbHelper.addValue(jaxbDestination, "OUTPUT2", ol.get(2));
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

}
