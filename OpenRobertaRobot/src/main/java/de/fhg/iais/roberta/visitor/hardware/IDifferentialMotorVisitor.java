package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;

public interface IDifferentialMotorVisitor<V> extends IMotorVisitor<V> {

    V visitDriveAction(DriveAction driveAction);

    V visitCurveAction(CurveAction curveAction);

    V visitTurnAction(TurnAction turnAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

}