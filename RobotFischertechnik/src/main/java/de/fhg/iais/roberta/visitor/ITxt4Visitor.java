package de.fhg.iais.roberta.visitor;


import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ServoOnForAction;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;

public interface ITxt4Visitor<V> extends IVisitor<V> {

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction);

    V visitMotorOmniDiffOnForAction(MotorOmniDiffOnForAction motorOmniDiffOnForAction);

    V visitMotorOmniTurnAction(MotorOmniTurnAction motorOmniTurnAction);

    V visitMotorOmniTurnForAction(MotorOmniTurnForAction motorOmniTurnForAction);

    V visitMotorOnForAction(MotorOnForAction motorOnForAction);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitEncoderReset(EncoderReset encoderReset);

    V visitServoOnForAction(ServoOnForAction servoOnForAction);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction);

    V visitGetLineSensor(GetLineSensor getLineSensor);
}
