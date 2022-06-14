package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_NOT_PUBLIC", blocklyNames = {"test_phrase_field_not_public"}, category = "EXPR")
public class TestPhraseFieldNotPublic<V> extends Phrase<V> {
    @NepoField(name = "MESSAGE")
    private final String message;

    public TestPhraseFieldNotPublic(BlocklyBlockProperties property, BlocklyComment comment, String message) {
        super(property, comment);
        this.message = message;
    }
}
