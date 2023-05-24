package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightOffAction;
import de.fhg.iais.roberta.syntax.action.light.RGBLedOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;

public interface IWeDoVisitor<V> extends IVisitor<V> {
    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitRGBLedOnAction(RGBLedOnAction rgbLedOnAction);

    V visitLightOffAction(LightOffAction lightOffAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);
}
