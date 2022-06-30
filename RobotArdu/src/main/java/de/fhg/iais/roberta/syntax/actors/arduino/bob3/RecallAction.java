package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "BOB3_RECALL", category = "ACTOR", blocklyNames = {"bob3Actions_recall"})
public final class RecallAction<V> extends Action<V> {

    public RecallAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }
}
