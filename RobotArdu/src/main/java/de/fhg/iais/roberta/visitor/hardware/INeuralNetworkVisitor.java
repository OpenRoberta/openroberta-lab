package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface INeuralNetworkVisitor<V> {
    default V visitNeuralNetworkSetup(NeuralNetworkSetup nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkInitRawData(NeuralNetworkInitRawData nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkAddRawData(NeuralNetworkAddRawData nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkTrain(NeuralNetworkTrain nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkAddClassifyData(NeuralNetworkAddClassifyData nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkInitClassifyData(NeuralNetworkInitClassifyData nn) {
        throw new DbcException("Not supported!");
    }

    default V visitNeuralNetworkClassify(NeuralNetworkClassify nn) {
        throw new DbcException("Not supported!");
    }
}
