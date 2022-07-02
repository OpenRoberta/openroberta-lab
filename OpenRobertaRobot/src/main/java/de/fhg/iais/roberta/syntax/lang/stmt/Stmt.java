package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

/**
 * the top class of all statements. There are two ways for a client to find out which kind of statement an {@link #Stmt}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Stmt<V> extends Phrase<V> {

    public Stmt(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
    }

}