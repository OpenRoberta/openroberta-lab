package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.actor.light.LightAction;
import de.fhg.iais.roberta.syntax.actor.light.LightStatusAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ILightVisitor<V> extends IHardwareVisitor<V> {

    default V visitLightAction(LightAction lightAction) {
        throw new DbcException("light action not implemented!");
    }

    default V visitLightStatusAction(LightStatusAction lightStatusAction) {
        throw new DbcException("status light action not implemented!");
    }
}