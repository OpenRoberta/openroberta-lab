package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface INeuralNetworkVisitor<V> {
    default V visitNeuralNetworkSetup(NeuralNetworkSetup<V> nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkInitRawData(NeuralNetworkInitRawData<V> nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkAddRawData(NeuralNetworkAddRawData<V> nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData<V> nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkTrain(NeuralNetworkTrain<V> nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkClassify(NeuralNetworkClassify<V> nn) {
        throw new DbcException("Not supported!");
    }
}
