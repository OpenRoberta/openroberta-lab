package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ILightVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link LightAction}.
     *
     * @param lightAction to be visited
     */
    V visitLightAction(LightAction<V> lightAction);

    /**
     * visit a {@link LightStatusAction}.
     *
     * @param lightStatusAction to be visited
     */
    V visitLightStatusAction(LightStatusAction<V> lightStatusAction);

}