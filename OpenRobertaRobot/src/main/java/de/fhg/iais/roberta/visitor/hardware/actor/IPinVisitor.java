package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IPinVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link PinWriteValueAction}.
     *
     * @param pinWriteValueAction to be visited
     */
    V visitPinWriteValueAction(PinWriteValueAction<V> pinWriteValueAction);
}
