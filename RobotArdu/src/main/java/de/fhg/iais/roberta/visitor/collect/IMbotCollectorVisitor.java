package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

/**
 * Collector for the MBot.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
 */
public interface IMbotCollectorVisitor extends ICollectorVisitor, IMbotVisitor<Void> {

    @Override
    default Void visitJoystick(Joystick<Void> joystick) {
        return null;
    }

    @Override
    default Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        return null;
    }

    @Override
    default Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        sendIRAction.getMessage().accept(this);
        return null;
    }

    @Override
    default Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        return null;
    }

    @Override
    default Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        ledMatrixImageAction.getValuesToDisplay().accept(this);
        return null;
    }

    @Override
    default Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        ledMatrixTextAction.getMsg().accept(this);
        return null;
    }

    @Override
    default Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        return null;
    }

    @Override
    default Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        ledMatrixImageShiftFunction.getImage().accept(this);
        ledMatrixImageShiftFunction.getPositions().accept(this);
        return null;
    }

    @Override
    default Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction) {
        ledMatrixImageInverFunction.getImage().accept(this);
        return null;
    }

    @Override
    default Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<Void> ledMatrixSetBrightnessAction) {
        ledMatrixSetBrightnessAction.getBrightness().accept(this);
        return null;
    }

    // following methods are used to specify unrelated defaults

    @Override
    default Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return ICollectorVisitor.super.visitClearDisplayAction(clearDisplayAction);
    }

    @Override
    default Void visitDriveAction(DriveAction<Void> driveAction) {
        return ICollectorVisitor.super.visitDriveAction(driveAction);
    }

    @Override
    default Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        return ICollectorVisitor.super.visitMotorOnAction(motorOnAction);
    }

    @Override
    default Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return ICollectorVisitor.super.visitPlayNoteAction(playNoteAction);
    }

    @Override
    default Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return ICollectorVisitor.super.visitPlayFileAction(playFileAction);
    }

    @Override
    default Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return ICollectorVisitor.super.visitMotorDriveStopAction(stopAction);
    }

    @Override
    default Void visitLightAction(LightAction<Void> lightAction) {
        return ICollectorVisitor.super.visitLightAction(lightAction);
    }

    @Override
    default Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        return ICollectorVisitor.super.visitShowTextAction(showTextAction);
    }

    @Override
    default Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return ICollectorVisitor.super.visitLightStatusAction(lightStatusAction);
    }

    @Override
    default Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return ICollectorVisitor.super.visitMotorSetPowerAction(motorSetPowerAction);
    }

    @Override
    default Void visitCurveAction(CurveAction<Void> curveAction) {
        return ICollectorVisitor.super.visitCurveAction(curveAction);
    }

    @Override
    default Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return ICollectorVisitor.super.visitMotorGetPowerAction(motorGetPowerAction);
    }

    @Override
    default Void visitTurnAction(TurnAction<Void> turnAction) {
        return ICollectorVisitor.super.visitTurnAction(turnAction);
    }

    @Override
    default Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return ICollectorVisitor.super.visitVolumeAction(volumeAction);
    }

    @Override
    default Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return ICollectorVisitor.super.visitMotorStopAction(motorStopAction);
    }

    @Override
    default Void visitToneAction(ToneAction<Void> toneAction) {
        return ICollectorVisitor.super.visitToneAction(toneAction);
    }
}
