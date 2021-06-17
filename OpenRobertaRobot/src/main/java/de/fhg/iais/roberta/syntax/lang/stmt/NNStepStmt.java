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
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

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
        return this.il;
    }

    public List<Var<V>> getOl() {
        return this.ol;
    }

    @Override
    public String toString() {
        return "nnStep()";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        helper.getDropdownFactory();
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 6);
        Phrase<V> i0 = helper.extractValue(values, new ExprParam("NN_STEP_INPUT0", BlocklyType.NUMBER_INT));
        Phrase<V> i1 = helper.extractValue(values, new ExprParam("NN_STEP_INPUT1", BlocklyType.NUMBER_INT));
        Phrase<V> i2 = helper.extractValue(values, new ExprParam("NN_STEP_INPUT2", BlocklyType.NUMBER_INT));
        List<Expr<V>> il = Arrays.asList(Jaxb2Ast.convertPhraseToExpr(i0), Jaxb2Ast.convertPhraseToExpr(i1), Jaxb2Ast.convertPhraseToExpr(i2));
        Phrase<V> o0 = helper.extractValue(values, new ExprParam("NN_STEP_OUTPUT0", BlocklyType.NUMBER_INT));
        Phrase<V> o1 = helper.extractValue(values, new ExprParam("NN_STEP_OUTPUT1", BlocklyType.NUMBER_INT));
        Phrase<V> o2 = helper.extractValue(values, new ExprParam("NN_STEP_OUTPUT2", BlocklyType.NUMBER_INT));
        Assert.isTrue(o0.getClass().equals(Var.class) && o1.getClass().equals(Var.class) && o2.getClass().equals(Var.class));
        final Var<V> v0 = (Var<V>) Jaxb2Ast.convertPhraseToExpr(o0);
        final Var<V> v1 = (Var<V>) Jaxb2Ast.convertPhraseToExpr(o1);
        final Var<V> v2 = (Var<V>) Jaxb2Ast.convertPhraseToExpr(o2);
        List<Var<V>> ol = Arrays.asList(v0, v1, v2);
        return NNStepStmt.make(il, ol, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.addValue(jaxbDestination, "NN_STEP_INPUT0", this.il.get(0));
        Ast2Jaxb.addValue(jaxbDestination, "NN_STEP_INPUT1", this.il.get(1));
        Ast2Jaxb.addValue(jaxbDestination, "NN_STEP_INPUT2", this.il.get(2));
        Ast2Jaxb.addValue(jaxbDestination, "NN_STEP_OUTPUT0", this.ol.get(0));
        Ast2Jaxb.addValue(jaxbDestination, "NN_STEP_OUTPUT1", this.ol.get(1));
        Ast2Jaxb.addValue(jaxbDestination, "NN_STEP_OUTPUT2", this.ol.get(2));
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

}
