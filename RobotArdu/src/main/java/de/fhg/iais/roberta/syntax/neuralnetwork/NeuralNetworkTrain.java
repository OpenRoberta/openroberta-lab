package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NEURAL_NETWORK_TRAIN", category = "STMT", blocklyNames = {"robActions_aifes_train"})
public final class NeuralNetworkTrain<V> extends Stmt<V> {
    public NeuralNetworkTrain(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

    public static <V> NeuralNetworkTrain<V> make(BlocklyProperties properties) {
        return new NeuralNetworkTrain<>(properties);
    }
}
