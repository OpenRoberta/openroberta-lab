package de.fhg.iais.roberta.visitor.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class ArduinoValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IArduinoVisitor<Void> {
    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = new HashMap<String, String>() {{
        put("COLOR_SENSING", "COLOR");
        put("TOUCH_SENSING", "TOUCH");
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
        put("MOISTURE_SENSING", SC.MOISTURE);
        put("MOTION_SENSING", SC.MOTION);
        put("KEYS_SENSING", SC.KEY);
        put("RFID_SENSING", SC.RFID);
        put("DROP_SENSING", SC.DROP);
        put("HUMIDITY_SENSING", SC.HUMIDITY);
        put("VOLTAGE_SENSING", SC.POTENTIOMETER);
        put("ENCODER_SENSING", SC.ENCODER);
    }};

    private final Map<String, String> sensorComponentTypeMap;

    public ArduinoValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
        this.sensorComponentTypeMap = SENSOR_COMPONENT_TYPE_MAP;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getR(), sensor.getG(), sensor.getB());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getDistance());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getGesture());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(clearDisplayAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(clearDisplayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        checkSensorPort(dropSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(dropSensor.getUserDefinedPort(), SC.DROP, dropSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        Sensor<Void> sensor = sensorGetSample.getSensor();
        requiredComponentVisited(sensorGetSample, sensor);
        // TODO remove once rfid library is supported for unowifirev2
        if ( sensor.getKind().hasName("RFID_SENSING") ) {
            addWarningToPhrase(sensorGetSample, "BLOCK_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(getSubFunct, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitGetSubFunct(getSubFunct);
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getHumidity());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getTemperature());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        checkSensorPort(humiditySensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(humiditySensor.getUserDefinedPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(indexOfFunct, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitIndexOfFunct(indexOfFunct);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        checkSensorPort(keysSensor);
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(lengthOfIsEmptyFunct, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitLengthOfIsEmptyFunct(lengthOfIsEmptyFunct);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        optionalComponentVisited(lightAction.getRgbLedColor());
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(lightAction.getPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(lightAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        if ( lightStatusAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(lightStatusAction.getUserDefinedPort());
            if ( usedConfigurationBlock == null ) {
                addErrorToPhrase(lightStatusAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            }
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(listGetIndex, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitListGetIndex(listGetIndex);
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(listSetIndex, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitListSetIndex(listSetIndex);
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getPressure());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LPS22HB, SC.LPS22HB));
        return null;
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getX(), sensor.getY(), sensor.getZ());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.x, sensor.y, sensor.z);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getX(), sensor.getY(), sensor.getZ());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(mathOnListFunct, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        checkSensorPort(moistureSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(moistureSensor.getUserDefinedPort(), SC.MOISTURE, moistureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        checkSensorPort(motionSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(motionSensor.getUserDefinedPort(), SC.MOTION, motionSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        MotionParam<Void> param = motorOnAction.getParam();
        requiredComponentVisited(param.getSpeed());
        Optional.ofNullable(motorOnAction.getDurationValue()).ifPresent(this::requiredComponentVisited);
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(motorOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else if ( usedConfigurationBlock.getComponentType().equals(SC.OTHER) && param.getDuration() != null ) {
            addErrorToPhrase(motorOnAction, "CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData<Void> nn) {
        requiredComponentVisited(nn, nn.getRawData());
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData<Void> nn) {
        requiredComponentVisited(nn, nn.getClassNumber());
        return null;
    }

    @Override
    public Void visitNeuralNetworkClassify(NeuralNetworkClassify<Void> nn) {
        requiredComponentVisited(nn, nn.probabilities);
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData<Void> nn) {
        return null;
    }

    @Override
    public Void visitNeuralNetworkSetup(NeuralNetworkSetup<Void> nn) {
        requiredComponentVisited(nn, nn.getNumberOfClasses(), nn.getNumberInputNeurons(), nn.getMaxNumberOfNeurons());
        return null;
    }

    @Override
    public Void visitNeuralNetworkTrain(NeuralNetworkTrain<Void> nn) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        checkSensorPort(pinGetValueSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(pinGetValueSensor.getUserDefinedPort(), SC.PIN_VALUE, pinGetValueSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        requiredComponentVisited(pinWriteValueAction, pinWriteValueAction.getValue());
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(pinWriteValueAction.getPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(pinWriteValueAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(pinWriteValueAction.getPort(), SC.ANALOG_PIN));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(playNoteAction.getPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(playNoteAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        checkSensorPort(pulseSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(pulseSensor.getUserDefinedPort(), SC.PULSE, pulseSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(relayAction.getPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(relayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        if ( !this.robotConfiguration.getRobotName().equals("unowifirev2") ) { // TODO remove when rfid library is supported for unowifirev2
            checkSensorPort(rfidSensor);
        } else {
            addWarningToPhrase(rfidSensor, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(rfidSensor.getUserDefinedPort(), SC.RFID, rfidSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        requiredComponentVisited(serialWriteAction, serialWriteAction.getValue());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.SERIAL, SC.SERIAL));
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(showTextAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(showTextAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        checkSensorPort(temperatureSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(temperatureSensor.getUserDefinedPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(toneAction.getPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(toneAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        checkSensorPort(voltageSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(voltageSensor.getUserDefinedPort(), SC.VOLTAGE, voltageSensor.getMode()));
        return null;
    }

    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());

        if ( configurationComponent == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }

        String expectedComponentType = this.sensorComponentTypeMap.get(sensor.getKind().getName());

        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.getComponentType()) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }
}
