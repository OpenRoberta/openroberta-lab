package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface NxtAstVisitor<V> extends AstVisitor<V>, AstSensorsVisitor<V>, AstActorCommunicationVisitor<V>, AstActorDisplayVisitor<V>,
    AstActorMotorVisitor<V>, AstActorLightVisitor<V>, AstActorSoundVisitor<V> {

    /**
     * visit a {@link LightSensorAction}.
     *
     * @param lightSensorAction to be visited
     */
    V visitLightSensorAction(LightSensorAction<V> lightSensorAction);
}
