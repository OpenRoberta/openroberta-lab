package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_WRONG_CONSTRUCTOR", blocklyNames = {"test_phrase_wrong_constructor"}, category = "EXPR")
public final class TestPhraseWrongConstructor extends Phrase {
    @NepoField(name = "TYPE")
    public final String type;

    @NepoValue(name = "VALUE", type = BlocklyType.ANY)
    public final Expr value;

    public TestPhraseWrongConstructor(
        BlocklyProperties property,

        Expr value, String type) {
        super(property);
        this.type = type;
        this.value = value;
    }
}
