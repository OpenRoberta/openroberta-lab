package de.fhg.iais.roberta.visitor.wedo;

import de.fhg.iais.roberta.syntax.action.wedo.LedOnAction;
import de.fhg.iais.roberta.syntax.expr.wedo.LedColor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface WeDoAstVisitor<V> {


     /**
     * visit a {@link GestureSensor}.
     *
     * @param gestureSensor phrase to be visited
     */
    V visitLedOnAction(LedOnAction<V> ledOnAction);

    V visitLedColor(LedColor<V> ledColor);

}
