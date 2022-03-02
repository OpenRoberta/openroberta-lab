package de.fhg.iais.roberta.syntax.actors.raspberrypi;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "T_STEP_FORWARD")
public class TStepForward<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.NUMBER, value = "1")
    public final String number;
    public TStepForward(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String number) {
        super(kind, properties, comment);
        this.number = number;
        setReadOnly();
    }
}