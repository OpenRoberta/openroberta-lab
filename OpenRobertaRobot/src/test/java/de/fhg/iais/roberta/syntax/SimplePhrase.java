package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;

@NepoPhrase(name = "SIMPLE_PHRASE", category = "STMT", blocklyNames = {"simple_phrase"})
public class SimplePhrase extends Phrase<Void> {
    public SimplePhrase() {
        super(BlocklyBlockProperties.make("SIMPLE_PHRASE", "1"), null);
    }
}
