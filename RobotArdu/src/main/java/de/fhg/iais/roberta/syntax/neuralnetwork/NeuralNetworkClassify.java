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

public class NeuralNetworkClassify<V> extends Stmt<V> {
    private final Expr<V> probabilities;

    private NeuralNetworkClassify(Expr<V> probabilities, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NEURAL_NETWORK_CLASSIFY"), properties, comment);
        this.probabilities = probabilities;
        setReadOnly();
    }

    public static <V> NeuralNetworkClassify<V> make(Expr<V> probabilities, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NeuralNetworkClassify<>(probabilities, properties, comment);
    }

    public Expr<V> getProbabilities() {
        return probabilities;
    }

    @Override
    public String toString() {
        return "NeuralNetworkClassify[]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitNeuralNetworkClassify(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Expr<V> probabilities = helper.convertPhraseToExpr(helper.extractValue(values, new ExprParam("NN_CLASS_PROBABILITIES", BlocklyType.CAPTURED_TYPE)));
        return NeuralNetworkClassify.make(probabilities, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
