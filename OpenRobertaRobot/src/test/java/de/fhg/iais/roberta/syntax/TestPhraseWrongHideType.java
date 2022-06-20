package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_WRONG_HIDE_TYPE", blocklyNames = {"test_phrase_wrong_hide_type"}, category = "EXPR")
public class TestPhraseWrongHideType<V> extends Phrase<V> {
    @NepoHide
    public final String hide;

    public TestPhraseWrongHideType(BlocklyBlockProperties property, BlocklyComment comment, String hide) {
        super(property, comment);
        this.hide = hide;
    }
}
