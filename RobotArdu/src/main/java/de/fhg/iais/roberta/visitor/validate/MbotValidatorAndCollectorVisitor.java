package de.fhg.iais.roberta.visitor.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public class MbotValidatorAndCollectorVisitor extends ArduinoDifferentialMotorValidatorAndCollectorVisitor implements IMbotVisitor<Void> {
    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("INFRARED_SENSING", SC.INFRARED);
        put("LIGHT_SENSING", SC.LIGHT);
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
    }});

    private final boolean isSim;

    public MbotValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders, boolean isSim) {
        super(brickConfiguration, beanBuilders);
        this.isSim = isSim;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        checkActorPort(clearDisplayAction, clearDisplayAction.port);
        usedHardwareBuilder.addUsedActor(new UsedActor(clearDisplayAction.port, SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        checkAndVisitMotionParam(driveAction, driveAction.param);
        checkLeftRightMotorPort(driveAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(driveAction.port, SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        checkAndVisitMotionParam(turnAction, turnAction.param);
        checkLeftRightMotorPort(turnAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(turnAction.port, SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        requiredComponentVisited(curveAction, curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed());
        Optional.ofNullable(curveAction.paramLeft.getDuration()).ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        Optional.ofNullable(curveAction.paramRight.getDuration()).ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        checkForZeroSpeedInCurve(curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed(), curveAction);
        checkLeftRightMotorPort(curveAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(curveAction.port, SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(stopAction.port, SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), SC.GEARED_MOTOR));
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        addWarningToPhrase(motorGetPowerAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorStopAction.getUserDefinedPort(), SC.GEARED_MOTOR));
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        addToPhraseIfUnsupportedInSim(sendIRAction, false, isSim);
        addToPhraseIfUnsupportedInSim(sendIRAction, false, isSim);
        requiredComponentVisited(sendIRAction, sendIRAction.message);
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.IR_TRANSMITTER));
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        addToPhraseIfUnsupportedInSim(receiveIRAction, true, isSim);
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.IR_TRANSMITTER));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction ledMatrixImageAction) {
        if ( ledMatrixImageAction.valuesToDisplay.toString().contains("ListCreate ") ||
            ledMatrixImageAction.valuesToDisplay.toString().contains("ListRepeat ") ) {
            addErrorToPhrase(ledMatrixImageAction, "BLOCK_USED_INCORRECTLY");
            return null;
        }
        requiredComponentVisited(ledMatrixImageAction, ledMatrixImageAction.valuesToDisplay);
        checkActorPort(ledMatrixImageAction, ledMatrixImageAction.port);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledMatrixImageAction.port, SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction ledMatrixTextAction) {
        requiredComponentVisited(ledMatrixTextAction, ledMatrixTextAction.msg);
        checkActorPort(ledMatrixTextAction, ledMatrixTextAction.port);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledMatrixTextAction.port, SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage ledMatrixImage) {
        usedHardwareBuilder.addUsedIDImage(ledMatrixImage.getProperty().getBlocklyId(), ledMatrixImage.image);
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction ledMatrixImageShiftFunction) {
        requiredComponentVisited(ledMatrixImageShiftFunction, ledMatrixImageShiftFunction.image, ledMatrixImageShiftFunction.positions);
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction ledMatrixImageInverFunction) {
        requiredComponentVisited(ledMatrixImageInverFunction, ledMatrixImageInverFunction.image);
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction ledMatrixSetBrightnessAction) {
        requiredComponentVisited(ledMatrixSetBrightnessAction, ledMatrixSetBrightnessAction.brightness);
        checkActorPort(ledMatrixSetBrightnessAction, ledMatrixSetBrightnessAction.port);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledMatrixSetBrightnessAction.port, SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        requiredComponentVisited(lightAction, lightAction.rgbLedColor);
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(playNoteAction.port, SC.BUZZER));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);
        usedHardwareBuilder.addUsedActor(new UsedActor(toneAction.port, SC.BUZZER));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        requiredComponentVisited(serialWriteAction, serialWriteAction.value);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        checkSensorPort(infraredSensor);
        return super.visitInfraredSensor(infraredSensor);
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        addToPhraseIfUnsupportedInSim(lightSensor, true, isSim);
        checkSensorPort(lightSensor);
        return super.visitLightSensor(lightSensor);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        return super.visitUltrasonicSensor(ultrasonicSensor);
    }

    private void checkActorPort(Action action, String port) {
        ConfigurationComponent usedConfigurationBlock = robotConfiguration.optConfigurationComponent("ORT_" + port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(action, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else {
            if ( !SC.LED_MATRIX.equals(usedConfigurationBlock.componentType) ) {
                addErrorToPhrase(action, "CONFIGURATION_ERROR_ACTOR_MISSING");
            }
        }
    }

    @Override
    protected void checkSensorPort(ExternalSensor sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent("ORT_" + sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(usedSensor.componentType) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }
}
