package de.fhg.iais.roberta.visitor.lang;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface IMotorVisitor<V> extends IVisitor<V> {
    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

}