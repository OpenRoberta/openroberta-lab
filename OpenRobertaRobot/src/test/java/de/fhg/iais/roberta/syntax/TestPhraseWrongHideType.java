package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase
public class TestPhraseWrongHideType<V> extends Phrase<V> {
    @NepoHide
    public final String hide;

    public TestPhraseWrongHideType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String hide) {
        super(kind, property, comment);
        this.hide = hide;
    }
}
