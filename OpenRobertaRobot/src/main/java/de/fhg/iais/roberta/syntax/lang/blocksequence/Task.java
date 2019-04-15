package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;

/**
 * the top class of all tasks. There are two ways for a client to find out which kind of task an {@link #Task}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Task<V> extends Phrase<V> {

    public Task(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
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

}
