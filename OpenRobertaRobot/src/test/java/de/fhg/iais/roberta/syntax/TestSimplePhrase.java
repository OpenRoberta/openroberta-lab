package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "SIMPLE_PHRASE", category = "STMT", blocklyNames = {"simple_phrase"})
public final class TestSimplePhrase extends Phrase {
    public TestSimplePhrase() {
        super(BlocklyProperties.make("SIMPLE_PHRASE", "1"));
    }
}
