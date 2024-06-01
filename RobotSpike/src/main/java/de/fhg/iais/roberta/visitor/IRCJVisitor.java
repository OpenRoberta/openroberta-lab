package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.rcj.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchKeySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.rcj.InductiveSensor;

/**
 * All spike-methods for code-generation function time should usually be Void,<br>
 * functions should return null.
 *
 * @param <V> usually is Void
 */
public interface IRCJVisitor<V> extends IVisitor<V> {
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

    V visitTouchKeySensor(TouchKeySensor touchKeySensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitInductiveSensor(InductiveSensor inductiveSensor);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitPlayToneAction(PlayToneAction playToneAction);

    V visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction);

    V visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction);

    V visitDisplayTextAction(DisplayTextAction displayTextAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitColorConst(ColorConst colorConst);

    V visitWaitTimeStmt(WaitTimeStmt waitTimeStmt);

}