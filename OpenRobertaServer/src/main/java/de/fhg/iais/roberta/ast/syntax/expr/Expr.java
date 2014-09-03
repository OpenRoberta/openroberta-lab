package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all expressions. There are two ways for a client to find out which kind of expresion an {@link #Expr}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Expr<V> extends Phrase<V> {

    /**
     * create a mutable expression of the given {@link Kind}
     * 
     * @param kind the kind of the expression,
     * @param disabled is the block,
     * @param comment of the user for the specific block
     */
    public Expr(Kind kind, boolean disabled, String comment) {
        super(kind, disabled, comment);
    }

    /**
     * get the precedence of the expression
     * 
     * @return the precedence
     */
    abstract public int getPrecedence();

    /**
     * get the association of the expression
     * 
     * @return the association
     */
    abstract public Assoc getAssoc();

}
