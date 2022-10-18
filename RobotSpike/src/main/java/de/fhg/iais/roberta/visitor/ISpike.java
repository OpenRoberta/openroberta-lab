package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.LedOffAction;
import de.fhg.iais.roberta.syntax.action.spike.LedOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.spike.Image;
import de.fhg.iais.roberta.syntax.spike.PredefinedImage;

public interface ISpike {
    Void visitMotorOnForAction(MotorOnForAction motorOnForAction);

    Void visitMotorOnAction(MotorOnAction motorOnAction);

    Void visitMotorStopAction(MotorStopAction motorStopAction);

    Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction);

    Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction);

    Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction);

    Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction);

    Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction);

    Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction);

    Void visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction);

    Void visitKeysSensor(KeysSensor keysSensor);

    Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    Void visitColorSensor(ColorSensor colorSensor);

    Void visitTouchSensor(TouchSensor touchSensor);

    Void visitGyroSensor(GyroSensor gyroSensor);

    Void visitGestureSensor(GestureSensor gestureSensor);

    Void visitGetSampleSensor(GetSampleSensor sensorGetSample);

    Void visitPlayNoteAction(PlayNoteAction playNoteAction);

    Void visitPlayToneAction(PlayToneAction playToneAction);

    Void visitLedOnAction(LedOnAction ledOnAction);

    Void visitLedOffAction(LedOffAction ledOffAction);

    Void visitDisplayImageAction(DisplayImageAction displayImageAction);

    Void visitDisplayTextAction(DisplayTextAction displayTextAction);

    Void visitDisplayClearAction(DisplayClearAction displayClearAction);

    Void visitImage(Image image);

    Void visitPredefinedImage(PredefinedImage predefinedImage);
}
