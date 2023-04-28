package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INano33BleSensorVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This class generates C++ code for the Arduino Nano 33 BLE.</b> <br>
 */
public class Nano33bleCppVisitor extends CommonArduinoCppVisitor implements INano33BleSensorVisitor<Void> {
    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public Nano33bleCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor sensor) {
        this.sb
            .append("(IMU.accelerationAvailable()?(IMU.readAcceleration(xAsFloat,yAsFloat,zAsFloat),")
            .append("___" + phrase2varValue(sensor.x))
            .append(" = (double) xAsFloat,")
            .append("___" + phrase2varValue(sensor.y))
            .append(" = (double) yAsFloat,")
            .append("___" + phrase2varValue(sensor.z))
            .append(" = (double) zAsFloat,1) : 0)");
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor sensor) {
        this.sb
            .append("(IMU.gyroscopeAvailable()?(IMU.readGyroscope(xAsFloat,yAsFloat,zAsFloat),")
            .append("___" + phrase2varValue(sensor.x))
            .append(" = (double) xAsFloat,")
            .append("___" + phrase2varValue(sensor.y))
            .append(" = (double) yAsFloat,")
            .append("___" + phrase2varValue(sensor.z))
            .append(" = (double) zAsFloat,1) : 0)");
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor sensor) {
        this.sb
            .append("(IMU.magneticFieldAvailable()?(IMU.readMagneticField(xAsFloat,yAsFloat,zAsFloat),")
            .append("___" + phrase2varValue(sensor.x))
            .append(" = (double) xAsFloat,")
            .append("___" + phrase2varValue(sensor.y))
            .append(" = (double) yAsFloat,")
            .append("___" + phrase2varValue(sensor.z))
            .append(" = (double) zAsFloat,1) : 0)");
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor sensor) {
        this.sb
            .append("(APDS.proximityAvailable()?(___") //
            .append(phrase2varValue(sensor.distance))
            .append(" = (double) APDS.readProximity(),1) : 0)");
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor sensor) {
        this.sb
            .append("(APDS.gestureAvailable()?(___") //
            .append(phrase2varValue(sensor.gesture))
            .append(" = (double) APDS.readGesture(),1) : 0)");
        return null;
    }

    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor sensor) {
        this.sb
            .append("(APDS.colorAvailable()?(APDS.readColor(rAsInt,gAsInt,bAsInt),")
            .append("___" + phrase2varValue(sensor.r))
            .append(" = (double) rAsInt,")
            .append("___" + phrase2varValue(sensor.g))
            .append(" = (double) gAsInt,")
            .append("___" + phrase2varValue(sensor.b))
            .append(" = (double) bAsInt,1) : 0)");
        return null;
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor sensor) {
        this.sb
            .append("(___") //
            .append(phrase2varValue(sensor.pressure))
            .append(" = (double) BARO.readPressure(),1)");
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor sensor) {
        this.sb
            .append("(___") //
            .append(phrase2varValue(sensor.temperature))
            .append(" = (double) HTS.readTemperature(),1)");
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor sensor) {
        this.sb
            .append("(___") //
            .append(phrase2varValue(sensor.humidity))
            .append(" = (double) HTS.readHumidity(),1)");
        return null;
    }

    @Override
    public Void visitNeuralNetworkSetup(NeuralNetworkSetup nn) {
        this.sb.append("// visitNeuralNetworkSetup");
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData nn) {
        String dataSetStr = this.configuration.getConfigurationComponent("Aifes").getProperty("AIFES_DATASET");
        String inputNeuronsStr = this.configuration.getConfigurationComponent("Aifes").getProperty("AIFES_NUMBER_INPUT_NEURONS");
        String outputNeuronsStr = this.configuration.getConfigurationComponent("Aifes").getProperty("AIFES_NUMBER_OUTPUT_NEURONS");
        this.sb.append("trainingData = 0;\n    trainingSet = 0;\n    targetData = 0;\n    targetSet = 0;\n    currentClassifySet = 0;\n");
        this.sb.append("\n    for (int i = 0; i < " + dataSetStr + "; i++){\n        for (int j = 0; j < " + inputNeuronsStr + "; j++) {\n            input_data[i][j] = 0.0;\n        }\n     }\n");
        this.sb.append("\n    for (int i = 0; i < " + dataSetStr + "; i++){\n        for (int j = 0; j < " + outputNeuronsStr + "; j++) {\n            target_data[i][j] = 0.0;\n        }\n     }");
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData nn) {
        this.sb.append("addInputData(" + nn.getValueNN(nn.rawData) + ");");
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData nn) {
        this.sb.append("addTargetData(" + nn.getValueNNTrain(nn.classNumber) + ");");
        return null;
    }

    @Override
    public Void visitNeuralNetworkTrain(NeuralNetworkTrain nn) {
        this.sb
            .append("AIFES_E_training_fnn_f32(&input_tensor,&target_tensor,&FNN,&FNN_TRAIN,&FNN_INIT_WEIGHTS,&output_train_tensor)");
        return null;
    }


    @Override
    public Void visitNeuralNetworkAddClassifyData(NeuralNetworkAddClassifyData nn) {
        this.sb.append("addClassifyData(" + nn.getValueNNClassify(nn.classNumber) + ");");
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitClassifyData(NeuralNetworkInitClassifyData nn) {
        String inputNeuronsStr = this.configuration.getConfigurationComponent("Aifes").getProperty("AIFES_NUMBER_INPUT_NEURONS");
        this.sb.append("\n    for (int i = 0; i < " + inputNeuronsStr + "; i++) {\n        classify_data[i] = 0.0;\n    }");
        return null;
    }

    @Override
    public Void visitNeuralNetworkClassify(NeuralNetworkClassify nn) {
        this.sb
            .append("(errorInference = AIFES_E_inference_fnn_f32(&classify_tensor,&FNN,&output_classify_tensor)==0?(___" + phrase2varValue(nn.probabilities) + ".assign(output_classify_data, output_classify_data + ___" + phrase2varValue(nn.probabilities) + ".size()),0):errorInference)");
        return null;
    }

    private String phrase2varValue(Expr phrase) {
        if ( phrase instanceof Var ) {
            return ((Var) phrase).name;
        } else {
            throw new DbcException("Phrase MUST be a Var: " + phrase);
        }
    }
}