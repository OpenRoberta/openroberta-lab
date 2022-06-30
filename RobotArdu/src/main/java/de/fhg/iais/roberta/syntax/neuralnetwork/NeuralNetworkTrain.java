package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "NEURAL_NETWORK_TRAIN", category = "STMT", blocklyNames = {"robActions_aifes_train"})
public final class NeuralNetworkTrain<V> extends Stmt<V> {
    public NeuralNetworkTrain(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }

    public static <V> NeuralNetworkTrain<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NeuralNetworkTrain<>(properties, comment);
    }
}
