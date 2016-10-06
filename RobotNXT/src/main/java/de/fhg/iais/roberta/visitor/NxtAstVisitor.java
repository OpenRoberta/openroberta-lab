package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.nxt.addition.ShowHelloWorldAction;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface NxtAstVisitor<V> extends AstVisitor<V> {
    public V visitShowHelloWorldAction(ShowHelloWorldAction<V> showHelloWorldAction);
}
