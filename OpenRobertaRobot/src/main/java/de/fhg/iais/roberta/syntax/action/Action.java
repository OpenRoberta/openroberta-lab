package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

public abstract class Action<V> extends Phrase<V> {

    public Action(BlocklyProperties properties) {
        super(properties);
    }

}
