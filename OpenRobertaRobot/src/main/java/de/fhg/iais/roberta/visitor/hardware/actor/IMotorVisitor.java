package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.actor.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.actor.motor.MotorStopAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IMotorVisitor<V> extends IHardwareVisitor<V> {

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

}