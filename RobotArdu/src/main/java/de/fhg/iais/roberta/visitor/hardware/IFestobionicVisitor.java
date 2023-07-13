package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.visitor.IVisitor;


public interface IFestobionicVisitor<V> extends IVisitor<V> {
    V visitLightAction(LedAction lightAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);
}
