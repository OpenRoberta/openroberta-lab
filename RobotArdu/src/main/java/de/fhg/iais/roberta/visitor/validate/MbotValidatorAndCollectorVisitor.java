package de.fhg.iais.roberta.visitor.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
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
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public class MbotValidatorAndCollectorVisitor extends ArduinoDifferentialMotorValidatorAndCollectorVisitor implements IMbotVisitor<Void> {
    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("INFRARED_SENSING", SC.INFRARED);
        put("LIGHT_SENSING", SC.LIGHT);
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
    }});

    public MbotValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        checkActorPort(clearDisplayAction, clearDisplayAction.getPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(clearDisplayAction.getPort(), SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkAndVisitMotionParam(driveAction, driveAction.getParam());
        checkLeftRightMotorPort(driveAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(driveAction.getPort(), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkAndVisitMotionParam(turnAction, turnAction.getParam());
        checkLeftRightMotorPort(turnAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(turnAction.getPort(), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        requiredComponentVisited(curveAction, curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed());
        Optional.ofNullable(curveAction.getParamLeft().getDuration()).ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        Optional.ofNullable(curveAction.getParamRight().getDuration()).ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        checkForZeroSpeedInCurve(curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed(), curveAction);
        checkLeftRightMotorPort(curveAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(curveAction.getPort(), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(stopAction.getPort(), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), SC.GEARED_MOTOR));
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        addWarningToPhrase(motorGetPowerAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorStopAction.getUserDefinedPort(), SC.GEARED_MOTOR));
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        requiredComponentVisited(sendIRAction, sendIRAction.getMessage());
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.IR_TRANSMITTER));
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.IR_TRANSMITTER));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        if ( ledMatrixImageAction.getValuesToDisplay().toString().contains("ListCreate ") ||
            ledMatrixImageAction.getValuesToDisplay().toString().contains("ListRepeat ") ) {
            addErrorToPhrase(ledMatrixImageAction, "BLOCK_USED_INCORRECTLY");
            return null;
        }
        requiredComponentVisited(ledMatrixImageAction, ledMatrixImageAction.getValuesToDisplay());
        checkActorPort(ledMatrixImageAction, ledMatrixImageAction.getPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(ledMatrixImageAction.getPort(), SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        requiredComponentVisited(ledMatrixTextAction, ledMatrixTextAction.getMsg());
        checkActorPort(ledMatrixTextAction, ledMatrixTextAction.getPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(ledMatrixTextAction.getPort(), SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        usedHardwareBuilder.addUsedIDImage(ledMatrixImage.getProperty().getBlocklyId(), ledMatrixImage.getImage());
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        requiredComponentVisited(ledMatrixImageShiftFunction, ledMatrixImageShiftFunction.getImage(), ledMatrixImageShiftFunction.getPositions());
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction) {
        requiredComponentVisited(ledMatrixImageInverFunction, ledMatrixImageInverFunction.getImage());
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<Void> ledMatrixSetBrightnessAction) {
        requiredComponentVisited(ledMatrixSetBrightnessAction, ledMatrixSetBrightnessAction.getBrightness());
        checkActorPort(ledMatrixSetBrightnessAction, ledMatrixSetBrightnessAction.getPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(ledMatrixSetBrightnessAction.getPort(), SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        requiredComponentVisited(lightAction, lightAction.getRgbLedColor());
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(playNoteAction.getPort(), SC.BUZZER));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        usedHardwareBuilder.addUsedActor(new UsedActor(toneAction.getPort(), SC.BUZZER));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        requiredComponentVisited(serialWriteAction, serialWriteAction.getValue());
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        checkSensorPort(infraredSensor);
        return super.visitInfraredSensor(infraredSensor);
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        return super.visitLightSensor(lightSensor);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        return super.visitUltrasonicSensor(ultrasonicSensor);
    }

    private void checkActorPort(Action<Void> action, String port) {
        ConfigurationComponent usedConfigurationBlock = robotConfiguration.optConfigurationComponent("ORT_" + port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(action, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else {
            if ( !SC.LED_MATRIX.equals(usedConfigurationBlock.getComponentType()) ) {
                addErrorToPhrase(action, "CONFIGURATION_ERROR_ACTOR_MISSING");
            }
        }
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent("ORT_" + sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(usedSensor.getComponentType()) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }
}
