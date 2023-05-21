package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.mbed.joycar.RgbLedOffActionJoycar;
import de.fhg.iais.roberta.syntax.action.mbed.joycar.RgbLedOnActionJoycar;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;

public interface IJoyCarVisitor<V> extends IMicrobitV2Visitor<V> {

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitDriveAction(DriveAction driveAction);

    V visitMotorDriveStopAction(MotorDriveStopAction motorDriveStopAction);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitGetLineSensor(GetLineSensor getLineSensor);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitTurnAction(TurnAction turnAction);

    V visitCurveAction(CurveAction curveAction);

    V visitRgbLedOnActionJoycar(RgbLedOnActionJoycar rgbLedOnActionJoycar);

    V visitRgbLedOffActionJoycar(RgbLedOffActionJoycar rgbLedOffActionJoycar);

    V visitMotorStopAction(MotorStopAction motorStopAction);
}
