package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.actor.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.actor.display.ShowTextAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IDisplayVisitor<V> extends IHardwareVisitor<V> {

    V visitClearDisplayAction(ClearDisplayAction clearDisplayAction);

    V visitShowTextAction(ShowTextAction showTextAction);

}