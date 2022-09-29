package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_WRONG_FIELD_TYPE", blocklyNames = {"test_phrase_wrong_field_type"}, category = "EXPR")
public final class TestPhraseWrongFieldType extends Phrase {
    @NepoField(name = "TYPE")
    public final int type;

    public TestPhraseWrongFieldType(BlocklyProperties property, int type) {
        super(property);
        this.type = type;
    }
}
