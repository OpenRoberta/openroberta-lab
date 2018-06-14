package de.fhg.iais.roberta.visitor.actor;

import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;

public interface AstActorLightVisitor<V> extends AstActorVisitor<V> {

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    V visitLightAction(LightAction<V> lightAction);

    /**
     * visit a {@link LedAction}.
     *
     * @param ledAction to be visited
     */
    V visitLedAction(LedAction<V> ledAction);

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    V visitLightStatusAction(LightStatusAction<V> lightStatusAction);

}