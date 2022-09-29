package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_CONSTRUCTOR_NOT_PUBLIC", blocklyNames = {"test_phrase_constructor_not_public"}, category = "EXPR")
public final class TestPhraseConstructorNotPublic extends Phrase {
    @NepoField(name = "TYPE")
    public final String type;

    @NepoValue(name = "VALUE", type = BlocklyType.ANY)
    public final Expr value;

    TestPhraseConstructorNotPublic(
        BlocklyProperties property,

        String type, Expr value) {
        super(property);
        this.type = type;
        this.value = value;
    }
}
