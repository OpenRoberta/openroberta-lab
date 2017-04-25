package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface NxtAstVisitor<V> extends AstSensorsVisitor<V>, AstActorCommunicationVisitor<V>, AstActorDisplayVisitor<V>, AstActorMotorVisitor<V>,
    AstActorLightVisitor<V>, AstActorSoundVisitor<V> {

    /**
     * visit a {@link LightSensorAction}.
     *
     * @param lightSensorAction to be visited
     */
    V visitLightSensorAction(LightSensorAction<V> lightSensorAction);
}
