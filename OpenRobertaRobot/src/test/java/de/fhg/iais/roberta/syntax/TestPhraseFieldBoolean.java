package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_FIELD_BOOLEAN", blocklyNames = {"test_phrase_field_boolean"}, category = "EXPR")
public final class TestPhraseFieldBoolean extends Expr {
    @NepoField(name = "FLAG")
    public final boolean flag;

    public TestPhraseFieldBoolean(BlocklyProperties properties, boolean flag) {
        super(properties);
        this.flag = flag;
    }
}
