package de.fhg.iais.roberta.visitor.nxt;

import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface NxtAstVisitor<V> extends AstVisitor<V> {

    /**
     * visit a {@link LightSensorAction}.
     *
     * @param lightSensorAction to be visited
     */
    V visitLightSensorAction(LightSensorAction<V> lightSensorAction);
}
