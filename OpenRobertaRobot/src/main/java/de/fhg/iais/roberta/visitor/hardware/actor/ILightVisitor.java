package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ILightVisitor<V> extends IHardwareVisitor<V> {

    V visitLightAction(LightAction<V> lightAction);

    V visitLightStatusAction(LightStatusAction<V> lightStatusAction);

}