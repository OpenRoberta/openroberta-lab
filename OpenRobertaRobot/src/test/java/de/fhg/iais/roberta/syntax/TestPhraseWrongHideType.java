package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_WRONG_HIDE_TYPE", blocklyNames = {"test_phrase_wrong_hide_type"}, category = "EXPR")
public final class TestPhraseWrongHideType extends Phrase {
    @NepoHide
    public final String hide;

    public TestPhraseWrongHideType(BlocklyProperties property, String hide) {
        super(property);
        this.hide = hide;
    }
}
