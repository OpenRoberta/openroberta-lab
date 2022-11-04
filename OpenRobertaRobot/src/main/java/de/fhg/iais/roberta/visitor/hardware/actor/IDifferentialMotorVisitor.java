package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.actor.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.actor.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.actor.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.actor.motor.differential.TurnAction;

public interface IDifferentialMotorVisitor<V> extends IMotorVisitor<V> {

    V visitDriveAction(DriveAction driveAction);

    V visitCurveAction(CurveAction curveAction);

    V visitTurnAction(TurnAction turnAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

}