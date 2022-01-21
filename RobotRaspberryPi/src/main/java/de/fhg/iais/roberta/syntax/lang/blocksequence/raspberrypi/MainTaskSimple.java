package de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Task;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "MAIN_TASK_SIMPLE")
public class MainTaskSimple<V> extends Task<V> {

    public MainTaskSimple(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        setReadOnly();
    }
}
