package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NEURAL_NETWORK_INIT_CLASSIFYDATA", category = "STMT", blocklyNames = {"robActions_aifes_initclassifydata"})
public final class NeuralNetworkInitClassifyData extends Stmt {
    public NeuralNetworkInitClassifyData(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}