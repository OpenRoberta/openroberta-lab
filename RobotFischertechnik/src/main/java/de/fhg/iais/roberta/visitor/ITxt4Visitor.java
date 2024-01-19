package de.fhg.iais.roberta.visitor;


import de.fhg.iais.roberta.syntax.action.MotorOmniOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;

public interface ITxt4Visitor<V> extends IVisitor<V> {

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorOmniOnAction(MotorOmniOnAction motorOmniOnAction);

    V visitMotorOmniOnForAction(MotorOmniOnForAction motorOmniOnForAction);

    V visitMotorOmniTurnAction(MotorOmniTurnAction motorOmniTurnAction);

    V visitMotorOmniTurnForAction(MotorOmniTurnForAction motorOmniTurnForAction);

    V visitMotorOnForAction(MotorOnForAction motorOnForAction);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitMotorOmniStopAction(MotorOmniStopAction motorOmniStopAction);

    V visitGetLineSensor(GetLineSensor getLineSensor);
}
