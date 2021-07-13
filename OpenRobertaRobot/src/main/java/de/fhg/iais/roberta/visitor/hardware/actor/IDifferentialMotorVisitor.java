package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;

public interface IDifferentialMotorVisitor<V> extends IMotorVisitor<V> {

    V visitDriveAction(DriveAction<V> driveAction);

    V visitCurveAction(CurveAction<V> curveAction);

    V visitTurnAction(TurnAction<V> turnAction);

    V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction);

}