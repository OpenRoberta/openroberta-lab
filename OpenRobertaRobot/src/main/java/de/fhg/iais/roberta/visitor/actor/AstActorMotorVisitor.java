package de.fhg.iais.roberta.visitor.actor;

import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;

public interface AstActorMotorVisitor<V> extends AstActorVisitor<V> {

    /**
     * visit a {@link DriveAction}.
     *
     * @param driveAction to be visited
     */
    V visitDriveAction(DriveAction<V> driveAction);

    /**
     * visit a {@link CurveAction}.
     *
     * @param turnAction to be visited
     */
    V visitCurveAction(CurveAction<V> curveAction);

    /**
     * visit a {@link TurnAction}.
     *
     * @param turnAction to be visited
     */
    V visitTurnAction(TurnAction<V> turnAction);

    /**
     * visit a {@link MotorGetPowerAction}.
     *
     * @param motorGetPowerAction to be visited
     */
    V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction);

    /**
     * visit a {@link MotorOnAction}.
     *
     * @param motorOnAction
     */
    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    /**
     * visit a {@link MotorSetPowerAction}.
     *
     * @param motorSetPowerAction
     */
    V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction);

    /**
     * visit a {@link MotorStopAction}.
     *
     * @param motorStopAction
     */
    V visitMotorStopAction(MotorStopAction<V> motorStopAction);

    /**
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction);

}