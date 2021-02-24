package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
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

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This class generates C++ code for the Arduino Nano 33 BLE.</b> <br>
 */
public class Nano33bleCppVisitor extends ArduinoCppVisitor {
    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public Nano33bleCppVisitor(List<List<Phrase<Void>>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor<Void> sensor) {
        this.sb
            .append("(IMU.accelerationAvailable()?(IMU.readAcceleration(xAsFloat,yAsFloat,zAsFloat),")
            .append("___" + sensor.getX().getValue())
            .append(" = (double) xAsFloat,")
            .append("___" + sensor.getY().getValue())
            .append(" = (double) yAsFloat,")
            .append("___" + sensor.getZ().getValue())
            .append(" = (double) zAsFloat,1) : 0)");
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<Void> sensor) {
        this.sb
            .append("(IMU.gyroscopeAvailable()?(IMU.readGyroscope(xAsFloat,yAsFloat,zAsFloat),")
            .append("___" + sensor.getX().getValue())
            .append(" = (double) xAsFloat,")
            .append("___" + sensor.getY().getValue())
            .append(" = (double) yAsFloat,")
            .append("___" + sensor.getZ().getValue())
            .append(" = (double) zAsFloat,1) : 0)");
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<Void> sensor) {
        this.sb
            .append("(IMU.magneticFieldAvailable()?(IMU.readMagneticField(xAsFloat,yAsFloat,zAsFloat),")
            .append("___" + sensor.getX().getValue())
            .append(" = (double) xAsFloat,")
            .append("___" + sensor.getY().getValue())
            .append(" = (double) yAsFloat,")
            .append("___" + sensor.getZ().getValue())
            .append(" = (double) zAsFloat,1) : 0)");
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor<Void> sensor) {
        this.sb
            .append("(APDS.proximityAvailable()?(___") //
            .append(sensor.getDistance().getValue())
            .append(" = (double) APDS.readProximity(),1) : 0)");
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor<Void> sensor) {
        this.sb
            .append("(APDS.gestureAvailable()?(___") //
            .append(sensor.getGesture().getValue())
            .append(" = (double) APDS.readGesture(),1) : 0)");
        return null;
    }

    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor<Void> sensor) {
        this.sb
            .append("(APDS.colorAvailable()?(APDS.readColor(rAsInt,gAsInt,bAsInt),")
            .append("___" + sensor.getR().getValue())
            .append(" = (double) rAsInt,")
            .append("___" + sensor.getG().getValue())
            .append(" = (double) gAsInt,")
            .append("___" + sensor.getB().getValue())
            .append(" = (double) bAsInt,1) : 0)");
        return null;
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor<Void> sensor) {
        this.sb
            .append("(___") //
            .append(sensor.getPressure().getValue())
            .append(" = (double) BARO.readPressure(),1)");
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor<Void> sensor) {
        this.sb
            .append("(___") //
            .append(sensor.getTemperature().getValue())
            .append(" = (double) HTS.readTemperature(),1)");
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor<Void> sensor) {
        this.sb
            .append("(___") //
            .append(sensor.getHumidity().getValue())
            .append(" = (double) HTS.readHumidity(),1)");
        return null;
    }

    @Override
    public Void visitNeuralNetworkSetup(NeuralNetworkSetup<Void> nn) {
        this.sb.append("// visitNeuralNetworkSetup");
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData<Void> nn) {
        this.sb.append("// visitNeuralNetworkInitRawData");
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData<Void> nn) {
        this.sb.append("// visitNeuralNetworkAddRawData");
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData<Void> nn) {
        this.sb.append("// visitNeuralNetworkAddTrainingsData");
        return null;
    }

    @Override
    public Void visitNeuralNetworkTrain(NeuralNetworkTrain<Void> nn) {
        this.sb.append("// visitNeuralNetworkTrain");
        return null;
    }

    @Override
    public Void visitNeuralNetworkClassify(NeuralNetworkClassify<Void> nn) {
        this.sb.append("// visitNeuralNetworkClassify");
        return null;
    }

}