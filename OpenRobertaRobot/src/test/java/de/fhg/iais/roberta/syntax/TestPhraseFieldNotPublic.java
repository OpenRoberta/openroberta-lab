package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_NOT_PUBLIC", blocklyNames = {"test_phrase_field_not_public"}, category = "EXPR")
public final class TestPhraseFieldNotPublic extends Phrase {
    @NepoField(name = "MESSAGE")
    private final String message;

    public TestPhraseFieldNotPublic(BlocklyProperties property, String message) {
        super(property);
        this.message = message;
    }
}
