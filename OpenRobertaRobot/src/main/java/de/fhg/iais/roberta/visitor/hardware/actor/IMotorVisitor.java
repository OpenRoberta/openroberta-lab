package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IMotorVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    default V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }


    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    default V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        throw new DbcException("Not supported!");
    }

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    default V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    default V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        throw new DbcException("Not supported!");
    }

}