package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        return NeuralNetworkInitRawData.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
