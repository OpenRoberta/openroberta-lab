package de.fhg.iais.roberta.visitor.actor;

import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;

public interface AstActorDisplayVisitor<V> extends AstActorVisitor<V> {

    /**
     * visit a {@link ClearDisplayAction}.
     *
     * @param clearDisplayAction to be visited
     */
    V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction);

    /**
     * visit a {@link ShowPictureAction}.
     *
     * @param showPictureAction
     */
    V visitShowPictureAction(ShowPictureAction<V> showPictureAction);

    /**
     * visit a {@link ShowTextAction}.
     *
     * @param showTextAction
     */
    V visitShowTextAction(ShowTextAction<V> showTextAction);

}