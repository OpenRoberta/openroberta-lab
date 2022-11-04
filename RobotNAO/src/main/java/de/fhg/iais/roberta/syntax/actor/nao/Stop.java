package de.fhg.iais.roberta.syntax.actor.nao;

import de.fhg.iais.roberta.syntax.actor.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "STOP", category = "ACTOR", blocklyNames = {"naoActions_stop"})
public final class Stop extends Action {

    public Stop(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

}
