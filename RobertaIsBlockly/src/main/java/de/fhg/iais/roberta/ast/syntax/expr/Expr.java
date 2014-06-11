package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all expressions. There are two ways for a client to find out which kind of expression an {@link #Expr}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Expr extends Phrase {

    public Expr(Kind kind) {
        super(kind);
    }

}
