package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class NeuralNetworkTrain<V> extends Stmt<V> {
    private NeuralNetworkTrain(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NEURAL_NETWORK_TRAIN"), properties, comment);
        setReadOnly();
    }

    public static <V> NeuralNetworkTrain<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NeuralNetworkTrain<>(properties, comment);
    }

    @Override
    public String toString() {
        return "NeuralNetworkTrain []";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitNeuralNetworkTrain(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return NeuralNetworkTrain.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
