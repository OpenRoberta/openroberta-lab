package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_WRONG_VALUE_TYPE", blocklyNames = {"test_phrase_wrong_value_type"}, category = "EXPR")
public class TestPhraseWrongValueType<V> extends Phrase<V> {

    @NepoField(name = "TYPE")
    public final String type;
    @NepoValue(name = "VALUE", type = BlocklyType.ANY)
    public final Action<V> value;

    public TestPhraseWrongValueType(
        BlocklyBlockProperties property,
        BlocklyComment comment,
        String type,
        Action<V> value) {
        super(property, comment);
        this.type = type;
        this.value = value;
    }
}
