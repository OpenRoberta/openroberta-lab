package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;

@NepoPhrase(name = "ACTOR", category = "ACTOR", blocklyNames = {"actor"})
public final class Actor extends Phrase<Void> {
    public Actor() {
        super(BlocklyBlockProperties.make("ACTOR", "1"), null);
    }
}
