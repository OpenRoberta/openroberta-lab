package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface IDisplayVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction);

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    V visitShowTextAction(ShowTextAction<V> showTextAction);

}