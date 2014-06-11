package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all statements. There are two ways for a client to find out which kind of statement an {@link #Stmt}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Stmt extends Phrase {

    public Stmt(Kind kind) {
        super(kind);
    }

}