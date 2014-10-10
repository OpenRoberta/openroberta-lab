package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;

/**
 * the top class of all tasks. There are two ways for a client to find out which kind of task an {@link #Task}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Task<V> extends Phrase<V> {

    public Task(Kind kind, BlocklyBlockProperties properties, BlocklyComment comment) {
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
