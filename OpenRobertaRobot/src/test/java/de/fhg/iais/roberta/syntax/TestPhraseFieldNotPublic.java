package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase
public class TestPhraseFieldNotPublic<V> extends Phrase<V> {
    @NepoField(name = "MESSAGE")
    private final String message;

    public TestPhraseFieldNotPublic(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String message) {
        super(kind, property, comment);
        this.message = message;
    }
}
