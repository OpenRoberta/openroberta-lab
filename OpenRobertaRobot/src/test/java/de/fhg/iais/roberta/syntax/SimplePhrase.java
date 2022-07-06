package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "SIMPLE_PHRASE", category = "STMT", blocklyNames = {"simple_phrase"})
public class SimplePhrase extends Phrase {
    public SimplePhrase() {
        super(BlocklyProperties.make("SIMPLE_PHRASE", "1"));
    }
}
