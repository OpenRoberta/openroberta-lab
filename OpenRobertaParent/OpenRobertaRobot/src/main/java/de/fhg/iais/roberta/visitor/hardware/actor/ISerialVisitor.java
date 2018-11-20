package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISerialVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link SerialWriteAction}.
     *
     * @param serialWriteAction to be visited
     */
    V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction);
}
