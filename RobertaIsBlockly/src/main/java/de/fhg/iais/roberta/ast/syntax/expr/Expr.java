package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all expressions.
 */
public abstract class Expr extends Phrase {

    public Expr(Kind kind) {
        super(kind);
    }

}
