package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.NepoMutation;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase
public class TestPhraseWrongMutationType<V> extends Phrase<V> {
    @NepoMutation
    public final String mutation;

    public TestPhraseWrongMutationType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String mutation) {
        super(kind, property, comment);
        this.mutation = mutation;
    }
}
