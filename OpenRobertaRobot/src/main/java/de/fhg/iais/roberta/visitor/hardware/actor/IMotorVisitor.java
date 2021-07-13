package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IMotorVisitor<V> extends IHardwareVisitor<V> {

    V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction);

    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction);

    V visitMotorStopAction(MotorStopAction<V> motorStopAction);

}