package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.Phrase;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IVisitor<V> {

    /**
     * Delegates visitable to matching visitT(T t) method from substructure
     *
     * @param visitable meant to be visited
     * @return output of delegated visit-method
     */
    V visit(Phrase visitable);

}
