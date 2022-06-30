package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

public abstract class Action<V> extends Phrase<V> {
    
    public Action(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
    }

}
