package de.fhg.iais.roberta.visitor;


import de.fhg.iais.roberta.syntax.action.MotorOnAction;

public interface ITxt4Visitor<V> extends IVisitor<V> {

    V visitMotorOnAction(MotorOnAction motorOnAction);

}
