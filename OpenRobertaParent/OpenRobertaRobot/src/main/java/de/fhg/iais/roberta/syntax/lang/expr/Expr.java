package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * the top class of all expressions. There are two ways for a client to find out which kind of expression an {@link #Expr}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Expr<V> extends Phrase<V> {

    /**
     * create a mutable expression of the given {@link BlockType}
     *
     * @param kind the kind of the expression,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public Expr(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
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

    abstract public BlocklyType getVarType();

}
