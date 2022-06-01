package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

public class TestPhraseNotAnnotated<V> extends Phrase<V> {
    public TestPhraseNotAnnotated(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
    }
}
