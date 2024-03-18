package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.spike.Image;
import de.fhg.iais.roberta.syntax.spike.PredefinedImage;

/**
 * All spike-methods for code-generation function time should usually be Void,<br>
 * functions should return null.
 *
 * @param <V> usually is Void
 */
public interface ISpikeVisitor<V> extends IVisitor<V> {
    V visitMotorOnForAction(MotorOnForAction motorOnForAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction);

    V visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction);

    V visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction);

    V visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction);

    V visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction);

    V visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction);

    V visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGestureSensor(GestureSensor gestureSensor);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitPlayToneAction(PlayToneAction playToneAction);

    V visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction);

    V visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction);

    V visitDisplayImageAction(DisplayImageAction displayImageAction);

    V visitDisplayTextAction(DisplayTextAction displayTextAction);

    V visitDisplayClearAction(DisplayClearAction displayClearAction);

    V visitImage(Image image);

    V visitPredefinedImage(PredefinedImage predefinedImage);

    V visitColorConst(ColorConst colorConst);

    V visitWaitTimeStmt(WaitTimeStmt waitTimeStmt);

}