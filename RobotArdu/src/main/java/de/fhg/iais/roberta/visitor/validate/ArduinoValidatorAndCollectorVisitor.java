package de.fhg.iais.roberta.visitor.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitClassifyData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class ArduinoValidatorAndCollectorVisitor extends MotorValidatorAndCollectorVisitor implements IArduinoVisitor<Void> {
    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
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
    }});

    public ArduinoValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }


    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(clearDisplayAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(clearDisplayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitDropSensor(DropSensor dropSensor) {
        checkSensorPort(dropSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(dropSensor.getUserDefinedPort(), SC.DROP, dropSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        Sensor sensor = sensorGetSample.sensor;
        requiredComponentVisited(sensorGetSample, sensor);
        // TODO remove once rfid library is supported for unowifirev2
        if ( sensor.getKind().hasName("RFID_SENSING") ) {
            addWarningToPhrase(sensorGetSample, "BLOCK_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroReset(GyroReset gyroReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroReset.sensorPort, SC.GYRO, SC.RESET));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        checkSensorPort(humiditySensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(humiditySensor.getUserDefinedPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.INFRARED) ) {
            addErrorToPhrase(infraredSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorPort(keysSensor);
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        if ( !lightAction.mode.toString().equals(BlocklyConstants.DEFAULT) ) {
            optionalComponentVisited(lightAction.rgbLedColor);
            if ( !this.robotConfiguration.isComponentTypePresent(SC.LED) ) {
                addErrorToPhrase(lightAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else {
                this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(lightAction.port, SC.LED));
            }
        } else {
            requiredComponentVisited(lightAction, lightAction.rgbLedColor);
            if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
                addErrorToPhrase(lightAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else {
                this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(lightAction.port, SC.RGBLED));
            }
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        if ( lightStatusAction.getInfos().getErrorCount() == 0 ) {
            if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
                addErrorToPhrase(lightStatusAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            }
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        if ( mathOnListFunct.param.get(0).toString().contains("ListCreate ") ) {
            addErrorToPhrase(mathOnListFunct, "BLOCK_USED_INCORRECTLY");
        }
        return super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor moistureSensor) {
        checkSensorPort(moistureSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(moistureSensor.getUserDefinedPort(), SC.MOISTURE, moistureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor motionSensor) {
        checkSensorPort(motionSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(motionSensor.getUserDefinedPort(), SC.MOTION, motionSensor.getMode()));
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData nn) {
        requiredComponentVisited(nn, nn.rawData);
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData nn) {
        requiredComponentVisited(nn, nn.classNumber);
        return null;
    }

    @Override
    public Void visitNeuralNetworkClassify(NeuralNetworkClassify nn) {
        requiredComponentVisited(nn, nn.probabilities);
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData nn) {
        return null;
    }

    @Override
    public Void visitNeuralNetworkSetup(NeuralNetworkSetup nn) {
        requiredComponentVisited(nn, nn.numberOfClasses, nn.numberInputNeurons, nn.maxNumberOfNeurons);
        return null;
    }

    @Override
    public Void visitNeuralNetworkTrain(NeuralNetworkTrain nn) {
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddClassifyData(NeuralNetworkAddClassifyData nn) {
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitClassifyData(NeuralNetworkInitClassifyData nn) {
        return null;
    }


    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        checkSensorPort(pinGetValueSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(pinGetValueSensor.getUserDefinedPort(), SC.PIN_VALUE, pinGetValueSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        requiredComponentVisited(pinWriteValueAction, pinWriteValueAction.value);
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(pinWriteValueAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(pinWriteValueAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(pinWriteValueAction.port, SC.ANALOG_PIN));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        ConfigurationComponent actor = this.robotConfiguration.optConfigurationComponent(playNoteAction.port);
        if ( actor == null ) {
            addErrorToPhrase(playNoteAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(playNoteAction.port, SC.BUZZER));
        return null;
    }

    @Override
    public Void visitPulseSensor(PulseSensor pulseSensor) {
        checkSensorPort(pulseSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(pulseSensor.getUserDefinedPort(), SC.PULSE, pulseSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction relayAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.RELAY) ) {
            addErrorToPhrase(relayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitRfidSensor(RfidSensor rfidSensor) {
        if ( !this.robotConfiguration.getRobotName().equals("unowifirev2") ) { // TODO remove when rfid library is supported for unowifirev2
            checkSensorPort(rfidSensor);
        } else {
            addWarningToPhrase(rfidSensor, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(rfidSensor.getUserDefinedPort(), SC.RFID, rfidSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        super.visitSerialWriteAction(serialWriteAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.SERIAL, SC.SERIAL));
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(showTextAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(showTextAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        checkSensorPort(temperatureSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(temperatureSensor.getUserDefinedPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);
        ConfigurationComponent actor = this.robotConfiguration.optConfigurationComponent(toneAction.port);
        if ( actor == null ) {
            addErrorToPhrase(toneAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(toneAction.port, actor.componentType));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor voltageSensor) {
        checkSensorPort(voltageSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(voltageSensor.getUserDefinedPort(), SC.VOLTAGE, voltageSensor.getMode()));
        return null;
    }

    protected void checkSensorPort(ExternalSensor sensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.componentType) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }
}
