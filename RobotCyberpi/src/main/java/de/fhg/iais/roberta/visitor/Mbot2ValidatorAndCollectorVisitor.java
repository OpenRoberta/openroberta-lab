package de.fhg.iais.roberta.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.constants.CyberpiConstants;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedOnActionWithIndex;
import de.fhg.iais.roberta.syntax.action.mbot2.DisplaySetColourAction;
import de.fhg.iais.roberta.syntax.action.mbot2.LedsOffAction;
import de.fhg.iais.roberta.syntax.action.mbot2.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.mbot2.PrintlnAction;
import de.fhg.iais.roberta.syntax.action.mbot2.QuadRGBLightOffAction;
import de.fhg.iais.roberta.syntax.action.mbot2.QuadRGBLightOnAction;
import de.fhg.iais.roberta.syntax.action.mbot2.Ultrasonic2LEDAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbot2.GyroResetAxis;
import de.fhg.iais.roberta.syntax.sensor.mbot2.Joystick;
import de.fhg.iais.roberta.syntax.sensor.mbot2.QuadRGBSensor;
import de.fhg.iais.roberta.syntax.sensor.mbot2.SoundRecord;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.visitor.validate.DifferentialMotorValidatorAndCollectorVisitor;


public class Mbot2ValidatorAndCollectorVisitor extends DifferentialMotorValidatorAndCollectorVisitor implements IMbot2Visitor<Void> {
    
    
    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = new HashMap<String, String>() {{
        put("SOUND_RECORD", SC.SOUND);
        put("QUAD_COLOR_SENSING", CyberpiConstants.MBUILD_QUADRGB);
        put("GYRO_AXIS_RESET", SC.GYRO);
    }};

    public Mbot2ValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        requiredComponentVisited(mainTask, mainTask.getVariables());
        return null;
    }
    
    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(clearDisplayAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(clearDisplayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        checkSensorPort(joystick);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(joystick.getUserDefinedPort(), SC.JOYSTICK, joystick.slot));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        checkSensorPort(keysSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.KEY));
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderSensor, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), configurationComponent.getComponentType()));
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        checkSensorPort(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSoundRecord(SoundRecord<Void> soundRecord) {
        checkSensorPort(soundRecord);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundRecord.getUserDefinedPort(), CyberpiConstants.RECORD, soundRecord.mode));
        return null;
    }

    @Override
    public Void visitPlayRecordingAction(PlayRecordingAction<Void> playRecordingAction) {
        checkActorPort(playRecordingAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(playRecordingAction.getUserDefinedPort(), CyberpiConstants.RECORD));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        checkSensorPort(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroResetAxis(GyroResetAxis<Void> gyroResetAxis) {
        checkSensorPort(gyroResetAxis);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroResetAxis.getUserDefinedPort(), SC.GYRO, gyroResetAxis.slot));
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        checkSensorPort(accelerometerSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDisplaySetColourAction(DisplaySetColourAction<Void> displaySetColourAction) {
        checkActorPort(displaySetColourAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(displaySetColourAction.getUserDefinedPort(), SC.DISPLAY));
        requiredComponentVisited(displaySetColourAction, displaySetColourAction.color);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, SC.ULTRASONIC));
        return null;
    }

    @Override
    public Void visitUltrasonic2LEDAction(Ultrasonic2LEDAction<Void> ultrasonic2LEDAction) {
        checkSensorPort(ultrasonic2LEDAction);
        requiredComponentVisited(ultrasonic2LEDAction, ultrasonic2LEDAction.brightness);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonic2LEDAction.getUserDefinedPort(), SC.ULTRASONIC, SC.LED));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonic2LEDAction.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, SC.ULTRASONIC));

        return null;
    }

    @Override
    public Void visitQuadRGBSensor(QuadRGBSensor<Void> quadRGBSensor) {
        checkSensorPort(quadRGBSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBSensor.getUserDefinedPort(), CyberpiConstants.QUADRGB, quadRGBSensor.mode));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBSensor.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, CyberpiConstants.QUADRGB));

        return null;
    }

    @Override
    public Void visitQuadRGBLightOnAction(QuadRGBLightOnAction<Void> quadRGBLightOnAction) {
        checkSensorPort(quadRGBLightOnAction);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOnAction.getUserDefinedPort(), CyberpiConstants.QUADRGB, SC.LED));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOnAction.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, CyberpiConstants.QUADRGB));
        usedMethodBuilder.addUsedMethod(Mbot2Methods.RGBASSTRING);
        requiredComponentVisited(quadRGBLightOnAction, quadRGBLightOnAction.color);
        return null;
    }

    @Override
    public Void visitQuadRGBLightOffAction(QuadRGBLightOffAction<Void> quadRGBLightOffAction) {
        checkSensorPort(quadRGBLightOffAction);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOffAction.getUserDefinedPort(), CyberpiConstants.QUADRGB, SC.LED));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(quadRGBLightOffAction.getUserDefinedPort(), CyberpiConstants.MBUILDSENSOR, CyberpiConstants.QUADRGB));
        return null;
    }

    @Override
    public Void visitLedOnActionWithIndex(LedOnActionWithIndex<Void> ledOnActionWithIndex) {
        checkActorPort(ledOnActionWithIndex);
        requiredComponentVisited(ledOnActionWithIndex, ledOnActionWithIndex.color);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledOnActionWithIndex.getUserDefinedPort(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitLedsOffAction(LedsOffAction<Void> ledsOffAction) {
        checkActorPort(ledsOffAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledsOffAction.getUserDefinedPort(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitLedBrightnessAction(LedBrightnessAction<Void> ledBrightnessAction) {
        checkActorPort(ledBrightnessAction);
        requiredComponentVisited(ledBrightnessAction, ledBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledBrightnessAction.getUserDefinedPort(), SC.RGBLED));
        return null;
    }

    @Override
    public Void visitPrintlnAction(PrintlnAction<Void> printlnAction) {
        checkActorPort(printlnAction);
        requiredComponentVisited(printlnAction, printlnAction.msg);
        usedHardwareBuilder.addUsedActor(new UsedActor(printlnAction.getUserDefinedPort(), SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
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
    public Void visitToneAction(ToneAction<Void> toneAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(toneAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(toneAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        if ( toneAction.getDuration().getKind().hasName("NUM_CONST") ) {
            double toneActionConst = Double.parseDouble(((NumConst<Void>) toneAction.getDuration()).getValue());
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(playNoteAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(playNoteAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        if ( volumeAction.getMode() == VolumeAction.Mode.SET ) {
            requiredComponentVisited(volumeAction, volumeAction.getVolume());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        addWarningToPhrase(playFileAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        addWarningToPhrase(motorGetPowerAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        addWarningToPhrase(motorSetPowerAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        super.visitDriveAction(driveAction);
        if ( driveAction.getParam().getDuration() != null ) {
            usedMethodBuilder.addUsedMethod(Mbot2Methods.DIFFDRIVEFOR);
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        super.visitCurveAction(curveAction);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            usedMethodBuilder.addUsedMethod(Mbot2Methods.DIFFDRIVEFOR);
        }
        return null;
    }

    private void checkActorPort(WithUserDefinedPort<Void> action) {
        Assert.isTrue(action instanceof Phrase, "checking Port of a non Phrase");
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            Phrase<Void> actionAsPhrase = (Phrase<Void>) action;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
    }

    private void checkSensorPort(WithUserDefinedPort<Void> sensor) {
        Assert.isTrue(sensor instanceof Phrase, "checking Port of a non Phrase");
        Phrase<Void> sensorAsSensor = (Phrase<Void>) sensor;

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

    private void checkSensorType(Phrase<Void> sensor, ConfigurationComponent configurationComponent) {
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        String typeWithoutSensing = sensor.getKind().getName().replace("_SENSING", "");
        if ( !(typeWithoutSensing.equalsIgnoreCase(configurationComponent.getComponentType())) ) {
            if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.getComponentType()) ) {
                addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
            }
        }
    }

    private ConfigurationComponent getSubComponent(String userDefinedPort) {
        for ( ConfigurationComponent component : this.robotConfiguration.getConfigurationComponentsValues() ) {
            try {
                for ( List<ConfigurationComponent> subComponents : component.getSubComponents().values() ) {
                    for ( ConfigurationComponent subComponent : subComponents ) {
                        if ( subComponent.getUserDefinedPortName().equals(userDefinedPort) ) {
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
