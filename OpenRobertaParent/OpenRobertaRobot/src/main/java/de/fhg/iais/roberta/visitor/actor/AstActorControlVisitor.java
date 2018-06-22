package de.fhg.iais.roberta.visitor.actor;

import de.fhg.iais.roberta.syntax.action.control.RelayAction;

public interface AstActorControlVisitor<V> extends AstActorVisitor<V> {

    /**
     * visit a {@link RelayAction}.
     *
     * @param relayAction to be visited
     */
    V visitRelayAction(RelayAction<V> relayAction);

}