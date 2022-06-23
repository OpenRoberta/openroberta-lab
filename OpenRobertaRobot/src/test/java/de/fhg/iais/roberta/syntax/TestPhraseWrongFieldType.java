package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "TEST_PHRASE_WRONG_FIELD_TYPE", blocklyNames = {"test_phrase_wrong_field_type"}, category = "EXPR")
public class TestPhraseWrongFieldType<V> extends Phrase<V> {
    @NepoField(name = "TYPE")
    public final int type;

    public TestPhraseWrongFieldType(BlocklyBlockProperties property, BlocklyComment comment, int type) {
        super(property, comment);
        this.type = type;
    }
}
