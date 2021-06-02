package de.fhg.iais.roberta.syntax.neuralnetwork;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class NeuralNetworkAddTrainingsData<V> extends Stmt<V> {
    private Expr<V> classNumber;

    private NeuralNetworkAddTrainingsData(Expr<V> classNumber, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NEURAL_NETWORK_ADD_TRAININGSDATA"), properties, comment);
        this.classNumber = classNumber;
        setReadOnly();
    }

    public static <V> NeuralNetworkAddTrainingsData<V> make(Expr<V> classNumber, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NeuralNetworkAddTrainingsData<>(classNumber, properties, comment);
    }

    public Expr<V> getClassNumber() {
        return this.classNumber;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("NeuralNetworkAddTrainingsData [ ").append(this.classNumber).append("]").toString();
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Expr<V> classNumber = Jaxb2Ast.convertPhraseToExpr(helper.extractValue(values, new ExprParam("NN_CLASS_NUMBER", BlocklyType.CAPTURED_TYPE)));
        return NeuralNetworkAddTrainingsData.make(classNumber, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, "NN_CLASS_NUMBER", getClassNumber());
        return jaxbDestination;
    }
}
