package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;

public interface IDifferentialMotorVisitor<V> extends IMotorVisitor<V> {

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
     * visit a {@link MotorDriveStopAction}.
     *
     * @param stopAction
     */
    V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction);

}