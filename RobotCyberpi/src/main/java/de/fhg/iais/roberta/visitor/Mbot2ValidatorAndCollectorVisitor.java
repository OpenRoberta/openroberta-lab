package de.fhg.iais.roberta.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.constants.CyberpiConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LedsOffAction;
import de.fhg.iais.roberta.syntax.action.mbot2.CommunicationReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbot2.CommunicationSendAction;
import de.fhg.iais.roberta.syntax.action.mbot2.DisplaySetColourAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedOnActionWithIndex;
import de.fhg.iais.roberta.syntax.action.mbot2.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.action.mbot2.PrintlnAction;
import de.fhg.iais.roberta.syntax.action.mbot2.QuadRGBLightOffAction;
import de.fhg.iais.roberta.syntax.action.mbot2.QuadRGBLightOnAction;
import de.fhg.iais.roberta.syntax.action.mbot2.Ultrasonic2LEDAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbot2.GyroResetAxis;
import de.fhg.iais.roberta.syntax.sensor.mbot2.Joystick;
import de.fhg.iais.roberta.syntax.sensor.mbot2.QuadRGBSensor;
import de.fhg.iais.roberta.syntax.sensor.mbot2.SoundRecord;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.validate.DifferentialMotorValidatorAndCollectorVisitor;


public class Mbot2ValidatorAndCollectorVisitor extends DifferentialMotorValidatorAndCollectorVisitor implements IMbot2Visitor<Void> {


    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = new HashMap<String, String>() {{
        put("SOUND_RECORD", SC.SOUND);
        put("QUAD_COLOR_SENSING", CyberpiConstants.MBUILD_QUADRGB);
        put("GYRO_AXIS_RESET", SC.GYRO);
    }};

    public Mbot2ValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        requiredComponentVisited(mainTask, mainTask.variables);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(clearDisplayAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(clearDisplayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitJoystick(Joystick joystick) {
        checkSensorPort(joystick);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(joystick.getUserDefinedPort(), SC.JOYSTICK, joystick.getSlot()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorPort(keysSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.KEY));
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderSensor, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), configurationComponent.componentType));
        }
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderReset.sensorPort);
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderReset, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderReset.sensorPort, configurationComponent.componentType));
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        checkSensorPort(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSoundRecord(SoundRecord soundRecord) {
        checkSensorPort(soundRecord);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundRecord.getUserDefinedPort(), CyberpiConstants.RECORD, soundRecord.getMode()));
        return null;
    }

    @Override
    public Void visitPlayRecordingAction(PlayRecordingAction playRecordingAction) {
        checkActorPort(playRecordingAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(playRecordingAction.getUserDefinedPort(), CyberpiConstants.RECORD));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        checkSensorPort(lightSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        checkSensorPort(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroResetAxis(GyroResetAxis gyroResetAxis) {
        checkSensorPort(gyroResetAxis);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroResetAxis.getUserDefinedPort(), SC.GYRO, gyroResetAxis.getSlot()));
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        checkSensorPort(accelerometerSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDisplaySetColourAction(DisplaySetColourAction displaySetColourAction) {
        checkActorPort(displaySetColourAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(displaySetColourAction.getUserDefinedPort(), SC.DISPLAY));
        requiredComponentVisited(displaySetColourAction, displaySetColourAction.color);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, SC.ULTRASONIC));
        return null;
    }

    @Override
    public Void visitUltrasonic2LEDAction(Ultrasonic2LEDAction ultrasonic2LEDAction) {
        checkSensorPort(ultrasonic2LEDAction);
        requiredComponentVisited(ultrasonic2LEDAction, ultrasonic2LEDAction.brightness);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonic2LEDAction.getUserDefinedPort(), SC.ULTRASONIC, SC.LED));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonic2LEDAction.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, SC.ULTRASONIC));

        return null;
    }

    @Override
    public Void visitQuadRGBSensor(QuadRGBSensor quadRGBSensor) {
        checkSensorPort(quadRGBSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBSensor.getUserDefinedPort(), CyberpiConstants.QUADRGB, quadRGBSensor.getMode()));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBSensor.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, CyberpiConstants.QUADRGB));

        return null;
    }

    @Override
    public Void visitQuadRGBLightOnAction(QuadRGBLightOnAction quadRGBLightOnAction) {
        checkSensorPort(quadRGBLightOnAction);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOnAction.getUserDefinedPort(), CyberpiConstants.QUADRGB, SC.LED));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOnAction.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, CyberpiConstants.QUADRGB));
        usedMethodBuilder.addUsedMethod(Mbot2Methods.RGBASSTRING);
        requiredComponentVisited(quadRGBLightOnAction, quadRGBLightOnAction.color);
        return null;
    }

    @Override
    public Void visitQuadRGBLightOffAction(QuadRGBLightOffAction quadRGBLightOffAction) {
        checkSensorPort(quadRGBLightOffAction);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOffAction.getUserDefinedPort(), CyberpiConstants.QUADRGB, SC.LED));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOffAction.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, CyberpiConstants.QUADRGB));
        return null;
    }

    @Override
    public Void visitLedOnActionWithIndex(LedOnActionWithIndex ledOnActionWithIndex) {
        checkActorPort(ledOnActionWithIndex);
        requiredComponentVisited(ledOnActionWithIndex, ledOnActionWithIndex.color);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledOnActionWithIndex.getUserDefinedPort(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitCommunicationSendAction(CommunicationSendAction communicationSendAction) {
        return null;
    }

    @Override
    public Void visitCommunicationReceiveAction(CommunicationReceiveAction communicationReceiveAction) {
        return null;
    }

    @Override
    public Void visitLedsOffAction(LedsOffAction ledsOffAction) {
        checkActorPort(ledsOffAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledsOffAction.getUserDefinedPort(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitLedBrightnessAction(LedBrightnessAction ledBrightnessAction) {
        checkActorPort(ledBrightnessAction);
        requiredComponentVisited(ledBrightnessAction, ledBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledBrightnessAction.getUserDefinedPort(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitPrintlnAction(PrintlnAction printlnAction) {
        checkActorPort(printlnAction);
        requiredComponentVisited(printlnAction, printlnAction.msg);
        usedHardwareBuilder.addUsedActor(new UsedActor(printlnAction.getUserDefinedPort(), SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(showTextAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(showTextAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(showTextAction, showTextAction.msg);
        requiredComponentVisited(showTextAction, showTextAction.x);
        requiredComponentVisited(showTextAction, showTextAction.y);
        usedHardwareBuilder.addUsedActor(new UsedActor(showTextAction.port, SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(toneAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(toneAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        if ( toneAction.duration.getKind().hasName("NUM_CONST") ) {
            double toneActionConst = Double.parseDouble(((NumConst) toneAction.duration).value);
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(playNoteAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(playNoteAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        requiredComponentVisited(setVolumeAction, setVolumeAction.volume);
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
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
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addWarningToPhrase(playFileAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        addWarningToPhrase(motorGetPowerAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        addWarningToPhrase(motorSetPowerAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        super.visitDriveAction(driveAction);
        if ( driveAction.param.getDuration() != null ) {
            usedMethodBuilder.addUsedMethod(Mbot2Methods.DIFFDRIVEFOR);
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        super.visitCurveAction(curveAction);
        if ( curveAction.paramLeft.getDuration() != null ) {
            usedMethodBuilder.addUsedMethod(Mbot2Methods.DIFFDRIVEFOR);
        }
        return null;
    }

    private void checkActorPort(WithUserDefinedPort action) {
        Assert.isTrue(action instanceof Phrase, "checking Port of a non Phrase");
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = (Phrase) action;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
    }

    private void checkSensorPort(WithUserDefinedPort sensor) {
        Assert.isTrue(sensor instanceof Phrase, "checking Port of a non Phrase");
        Phrase sensorAsSensor = (Phrase) sensor;

        String userDefinedPort = sensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(userDefinedPort);
        if ( configurationComponent == null ) {
            configurationComponent = getSubComponent(userDefinedPort);
            if ( configurationComponent == null ) {
                addErrorToPhrase(sensorAsSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
                return;
            }
        }
        checkSensorType(sensorAsSensor, configurationComponent);
    }

    private void checkSensorType(Phrase sensor, ConfigurationComponent configurationComponent) {
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        String typeWithoutSensing = sensor.getKind().getName().replace("_SENSING", "");
        if ( !(typeWithoutSensing.equalsIgnoreCase(configurationComponent.componentType)) ) {
            if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.componentType) ) {
                addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
            }
        }
    }

    private ConfigurationComponent getSubComponent(String userDefinedPort) {
        for ( ConfigurationComponent component : this.robotConfiguration.getConfigurationComponentsValues() ) {
            try {
                for ( List<ConfigurationComponent> subComponents : component.getSubComponents().values() ) {
                    for ( ConfigurationComponent subComponent : subComponents ) {
                        if ( subComponent.userDefinedPortName.equals(userDefinedPort) ) {
                            return subComponent;
                        }
                    }
                }
            } catch ( UnsupportedOperationException e ) {
                continue;
            }
        }
        return null;
    }
}
