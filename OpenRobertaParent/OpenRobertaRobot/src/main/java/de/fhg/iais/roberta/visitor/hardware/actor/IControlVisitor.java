package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.control.RelayAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IControlVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link RelayAction}.
     *
     * @param relayAction to be visited
     */
    V visitRelayAction(RelayAction<V> relayAction);

}