package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOB3_RECALL", category = "ACTOR", blocklyNames = {"bob3Actions_recall"})
public final class RecallAction extends Action {

    public RecallAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
