package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public class ArduinoCppVisitor extends CommonArduinoCppVisitor {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public ArduinoCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    public final Void visitNeuralNetworkSetup(NeuralNetworkSetup nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkTrain(NeuralNetworkTrain nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkAddClassifyData(NeuralNetworkAddClassifyData nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkInitClassifyData(NeuralNetworkInitClassifyData nn) {
        throw new DbcException("only supported for nano33ble");
    }

    public final Void visitNeuralNetworkClassify(NeuralNetworkClassify nn) {
        throw new DbcException("only supported for nano33ble");
    }

}
