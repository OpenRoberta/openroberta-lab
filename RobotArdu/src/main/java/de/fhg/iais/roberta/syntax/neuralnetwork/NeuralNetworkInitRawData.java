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

public class NeuralNetworkInitRawData<V> extends Stmt<V> {
    private NeuralNetworkInitRawData(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NEURAL_NETWORK_INIT_RAWDATA"), properties, comment);
        setReadOnly();
    }

    public static <V> NeuralNetworkInitRawData<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NeuralNetworkInitRawData<>(properties, comment);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("NeuralNetworkInitRawData []").toString();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitNeuralNetworkInitRawData(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return NeuralNetworkInitRawData.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
