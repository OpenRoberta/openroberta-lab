package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_WRONG_VALUE_TYPE", blocklyNames = {"test_phrase_wrong_value_type"}, category = "EXPR")
public final class TestPhraseWrongValueType extends Phrase {

    @NepoField(name = "TYPE")
    public final String type;
    @NepoValue(name = "VALUE", type = BlocklyType.ANY)
    public final Action value;

    public TestPhraseWrongValueType(
        BlocklyProperties property,

        String type,
        Action value) {
        super(property);
        this.type = type;
        this.value = value;
    }
}
