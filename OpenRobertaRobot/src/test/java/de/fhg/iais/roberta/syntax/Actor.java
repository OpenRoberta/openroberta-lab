package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "ACTOR", category = "ACTOR", blocklyNames = {"actor"})
public final class Actor extends Phrase {
    public Actor() {
        super(BlocklyProperties.make("ACTOR", "1"));
    }
}
