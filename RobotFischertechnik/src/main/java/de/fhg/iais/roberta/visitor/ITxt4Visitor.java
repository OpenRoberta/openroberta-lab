package de.fhg.iais.roberta.visitor;


import de.fhg.iais.roberta.syntax.action.MotorOmniOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;

public interface ITxt4Visitor<V> extends IVisitor<V> {

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorOmniOnAction(MotorOmniOnAction motorOmniOnAction);

    V visitMotorOmniTurnAction(MotorOmniTurnAction motorOmniTurnAction);

    V visitMotorOmniTurnForAction(MotorOmniTurnForAction motorOmniTurnForAction);
}
