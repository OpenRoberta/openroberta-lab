package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;

/**
 * the top class of all action class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immutable. The logic to
 * achieve that is in the {@link Phrase} class. There are two ways for a client to find out which kind a {@link #Action}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Action<V> extends Phrase<V> {

    /**
     * This constructor set the kind of the action object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param kind of the the action object used in AST,
     * @param properties of the block,
     * @param comment of the user for the specific block
     */
    public Action(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
    }

}
