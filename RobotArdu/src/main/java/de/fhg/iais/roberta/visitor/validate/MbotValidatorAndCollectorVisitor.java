package de.fhg.iais.roberta.visitor.validate;

import java.util.Optional;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
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
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public class MbotValidatorAndCollectorVisitor extends ArduinoDifferentialMotorValidatorAndCollectorVisitor implements IMbotVisitor<Void> {

    public MbotValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }


    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkAndVisitMotionParam(driveAction, driveAction.getParam());
        checkLeftRightMotorPort(driveAction);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkAndVisitMotionParam(turnAction, turnAction.getParam());
        checkLeftRightMotorPort(turnAction);
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        requiredComponentVisited(curveAction, curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed());
        Optional.ofNullable(curveAction.getParamLeft().getDuration()).ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        Optional.ofNullable(curveAction.getParamRight().getDuration()).ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        checkForZeroSpeedInCurve(curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed(), curveAction);
        checkLeftRightMotorPort(curveAction);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
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
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        requiredComponentVisited(sendIRAction, sendIRAction.getMessage());
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        requiredComponentVisited(ledMatrixImageAction, ledMatrixImageAction.getValuesToDisplay());
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        requiredComponentVisited(ledMatrixTextAction, ledMatrixTextAction.getMsg());
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        requiredComponentVisited(ledMatrixImageShiftFunction, ledMatrixImageShiftFunction.getImage());
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
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        return null;
    }


}
