package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "IR_RECV")
public class ReceiveIRAction<V> extends Action<V> {

    public ReceiveIRAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        setReadOnly();
    }

    private static <V> ReceiveIRAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ReceiveIRAction<>(BlockTypeContainer.getByName("IR_RECV"), properties, comment);
    }
}