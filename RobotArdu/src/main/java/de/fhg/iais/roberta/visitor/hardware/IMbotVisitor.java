package de.fhg.iais.roberta.visitor.hardware;

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
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface IMbotVisitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitSendIRAction(SendIRAction sendIRAction);

    V visitReceiveIRAction(ReceiveIRAction receiveIRAction);

    V visitLEDMatrixImageAction(LEDMatrixImageAction ledMatrixImageAction);

    V visitLEDMatrixTextAction(LEDMatrixTextAction ledMatrixTextAction);

    V visitLEDMatrixImage(LEDMatrixImage ledMatrixImage);

    V visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction ledMatrixImageShiftFunction);

    V visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction ledMatrixImageInverFunction);

    V visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction ledMatrixSetBrightnessAction);

    V visitLightAction(LightAction lightAction);

    V visitLightStatusAction(LightStatusAction lightStatusAction);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitDriveAction(DriveAction driveAction);

    V visitCurveAction(CurveAction curveAction);

    V visitTurnAction(TurnAction turnAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitLightSensor(LightSensor lightSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);
}
