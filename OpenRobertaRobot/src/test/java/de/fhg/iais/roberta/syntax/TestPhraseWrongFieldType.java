package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase
public class TestPhraseWrongFieldType<V> extends Phrase<V> {
    @NepoField(name = "TYPE")
    public final int type;

    public TestPhraseWrongFieldType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, int type) {
        super(kind, property, comment);
        this.type = type;
    }
}
