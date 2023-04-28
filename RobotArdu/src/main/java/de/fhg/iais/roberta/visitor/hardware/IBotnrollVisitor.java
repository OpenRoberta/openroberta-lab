package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface IBotnrollVisitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitShowTextAction(ShowTextAction showTextAction);

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitLightAction(LightAction lightAction);

    V visitToneAction(ToneAction toneAction);

    V visitPlayNoteAction(PlayNoteAction playNoteAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitDriveAction(DriveAction driveAction);

    V visitCurveAction(CurveAction curveAction);

    V visitTurnAction(TurnAction turnAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitLightSensor(LightSensor lightSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitVoltageSensor(VoltageSensor voltageSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);
}
