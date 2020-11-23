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
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class NeuralNetworkSetup<V> extends Stmt<V> {
    private Expr<V> numberOfClasses;
    private Expr<V> numberInputNeurons;
    private Expr<V> maxNumberOfNeurons;

    private NeuralNetworkSetup(
        Expr<V> numberOfClasses,
        Expr<V> numberInputNeurons,
        Expr<V> maxNumberOfNeurons,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NEURAL_NETWORK_SETUP"), properties, comment);
        this.numberOfClasses = numberOfClasses;
        this.numberInputNeurons = numberInputNeurons;
        this.maxNumberOfNeurons = maxNumberOfNeurons;
        setReadOnly();
    }

    public static <V> NeuralNetworkSetup<V> make(
        Expr<V> numberOfClasses,
        Expr<V> numberInputNeurons,
        Expr<V> maxNumberOfNeurons,
        BlocklyBlockProperties properties,
        BlocklyComment comment) //
    {
        return new NeuralNetworkSetup<>(numberOfClasses, numberInputNeurons, maxNumberOfNeurons, properties, comment);
    }

    /**
     * @return the number of classes of this neural network (it's an classifier network)
     */
    public Expr<V> getNumberOfClasses() {
        return this.numberOfClasses;
    }

    public Expr<V> getNumberInputNeurons() {
        return numberInputNeurons;
    }

    public Expr<V> getMaxNumberOfNeurons() {
        return maxNumberOfNeurons;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("NeuralNetworkNew [ ")
            .append(this.numberOfClasses)
            .append(this.numberInputNeurons)
            .append(this.maxNumberOfNeurons)
            .append("]")
            .toString();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitNeuralNetworkSetup(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 4);
        Expr<V> numberOfClasses = helper.convertPhraseToExpr(helper.extractValue(values, new ExprParam("NN_NUMBER_OF_CLASSES", BlocklyType.NUMBER)));
        Expr<V> numberInputNeurons = helper.convertPhraseToExpr(helper.extractValue(values, new ExprParam("NN_NUMBER_INPUT_NEURONS", BlocklyType.NUMBER)));
        Expr<V> maxNumberOfNeurons = helper.convertPhraseToExpr(helper.extractValue(values, new ExprParam("NN_MAX_NUMBER_OF_NEURONS", BlocklyType.NUMBER)));
        return NeuralNetworkSetup
            .make(numberOfClasses, numberInputNeurons, maxNumberOfNeurons, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, "NN_NUMBER_OF_CLASSES", getNumberOfClasses());
        Ast2Jaxb.addValue(jaxbDestination, "NN_NUMBER_INPUT_NEURONS", getNumberInputNeurons());
        Ast2Jaxb.addValue(jaxbDestination, "NN_MAX_NUMBER_OF_NEURONS", getMaxNumberOfNeurons());
        return jaxbDestination;
    }
}
