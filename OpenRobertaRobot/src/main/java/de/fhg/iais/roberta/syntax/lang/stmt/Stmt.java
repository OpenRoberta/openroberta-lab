package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

/**
 * the top class of all statements. There are two ways for a client to find out which kind of statement an {@link #Stmt}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Stmt extends Phrase {

    public Stmt(BlocklyProperties properties) {
        super(properties);
    }

}