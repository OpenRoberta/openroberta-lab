package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;

public class SimplePhrase extends Phrase<Void> {
    public SimplePhrase() {
        super(null, BlocklyBlockProperties.make("1", "1"), null);
    }
}
