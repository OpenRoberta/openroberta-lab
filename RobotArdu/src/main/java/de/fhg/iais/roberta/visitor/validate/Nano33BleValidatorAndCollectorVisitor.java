package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
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
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.hardware.INano33BleSensorVisitor;

public class Nano33BleValidatorAndCollectorVisitor extends ArduinoValidatorAndCollectorVisitor implements INano33BleSensorVisitor<Void> {

    public Nano33BleValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }


    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor sensor) {
        requiredComponentVisited(sensor, sensor.r, sensor.g, sensor.b);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor sensor) {
        requiredComponentVisited(sensor, sensor.distance);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor sensor) {
        requiredComponentVisited(sensor, sensor.gesture);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor sensor) {
        requiredComponentVisited(sensor, sensor.humidity);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor sensor) {
        requiredComponentVisited(sensor, sensor.temperature);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor sensor) {
        requiredComponentVisited(sensor, sensor.pressure);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LPS22HB, SC.LPS22HB));
        return null;
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor sensor) {
        requiredComponentVisited(sensor, sensor.x, sensor.y, sensor.z);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor sensor) {
        requiredComponentVisited(sensor, sensor.x, sensor.y, sensor.z);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor sensor) {
        requiredComponentVisited(sensor, sensor.x, sensor.y, sensor.z);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }


    @Override
    public Void visitNeuralNetworkAddClassifyData(NeuralNetworkAddClassifyData nn) {
        checkConfBlockForAifes(nn);
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData nn) {
        checkConfBlockForAifes(nn);
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData nn) {
        checkConfBlockForAifes(nn);
        return null;
    }

    @Override
    public Void visitNeuralNetworkClassify(NeuralNetworkClassify nn) {
        ConfigurationComponent Aifes = this.robotConfiguration.optConfigurationComponent("Aifes");
        if ( Aifes == null ) {
            addErrorToPhrase(nn, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitClassifyData(NeuralNetworkInitClassifyData nn) {
        checkConfBlockForAifes(nn);
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData nn) {
        checkConfBlockForAifes(nn);
        return null;
    }

    @Override
    public Void visitNeuralNetworkSetup(NeuralNetworkSetup nn) {
        checkConfBlockForAifes(nn);
        return null;
    }

    @Override
    public Void visitNeuralNetworkTrain(NeuralNetworkTrain nn) {
        ConfigurationComponent Aifes = this.robotConfiguration.optConfigurationComponent("Aifes");
        if ( Aifes == null ) {
            addErrorToPhrase(nn, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        return null;
    }

    public void checkConfBlockForAifes(Stmt nn) {
        ConfigurationComponent Aifes = this.robotConfiguration.optConfigurationComponent("Aifes");
        if ( Aifes == null ) {
            addErrorToPhrase(nn, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        if ( Aifes != null ) {
            checkValueInConBlockAifes(nn);
        }
    }

    protected void checkValueInConBlockAifes(Stmt nn) {
        ConfigurationComponent Aifes = this.robotConfiguration.optConfigurationComponent("Aifes");
        String[] ConfigElements = {"AIFES_NUMBER_INPUT_NEURONS", "AIFES_NUMBER_HIDDENLAYERS_NEURONS", "AIFES_NUMBER_OUTPUT_NEURONS", "AIFES_FNN_LAYERS", "AIFES_LEARNINGRATE", "AIFES_MOMENTUM", "AIFES_DATASET", "AIFES_EPOCHS", "AIFES_MAX_WEIGHT", "AIFES_MIN_WEIGHT"};
        String[] ConfigNeuronsLayers = {"AIFES_NUMBER_INPUT_NEURONS", "AIFES_NUMBER_HIDDENLAYERS_NEURONS", "AIFES_NUMBER_OUTPUT_NEURONS", "AIFES_FNN_LAYERS", "AIFES_DATASET"};
        for ( String Element : ConfigNeuronsLayers ) {
            String ElementStr = Aifes.getProperty(Element);
            int ElementInt = Integer.parseInt(ElementStr);
            if ( (ElementInt < 0) || (ElementInt > 1000) ) {
                addErrorToPhrase(nn, "AIFES_CONFIGURATION_ERROR_WRONG_VALUES");
            }
        }
        if ( Integer.parseInt(Aifes.getProperty("AIFES_EPOCHS")) < 0 ) {
            addErrorToPhrase(nn, "AIFES_CONFIGURATION_ERROR_WRONG_VALUES");
        }
    }

}
