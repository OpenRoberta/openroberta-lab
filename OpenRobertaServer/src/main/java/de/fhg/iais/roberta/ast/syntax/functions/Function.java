package de.fhg.iais.roberta.ast.syntax.functions;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;

/**
 * the top class of all functions. There are two ways for a client to find out which kind of function {@link #Function}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Function<V> extends Phrase<V> {

    /**
     * create a mutable function of the given {@link Kind}
     *
     * @param kind the kind of the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public Function(Kind kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);

    }

    /**
     * get the precedence of the function
     *
     * @return the precedence
     */
    abstract public int getPrecedence();

    /**
     * get the association of the function
     *
     * @return the association
     */
    abstract public Assoc getAssoc();

}
