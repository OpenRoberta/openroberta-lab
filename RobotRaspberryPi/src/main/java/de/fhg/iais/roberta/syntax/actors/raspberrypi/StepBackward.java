package de.fhg.iais.roberta.syntax.actors.raspberrypi;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "STEP_BACKWARD")
public class StepBackward<V> extends Action<V> {
    public StepBackward(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        setReadOnly();
    }
}