package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "NEURAL_NETWORK_INIT_CLASSIFYDATA", category = "STMT", blocklyNames = {"robActions_aifes_initclassifydata"})
public final class NeuralNetworkInitClassifyData extends Stmt {
    private NeuralNetworkInitClassifyData(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
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
    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        return new NeuralNetworkInitClassifyData(Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}