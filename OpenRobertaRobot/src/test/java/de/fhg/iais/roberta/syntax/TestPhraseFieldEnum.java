package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_FIELD_ENUM", blocklyNames = {"test_phrase_field_enum"}, category = "EXPR")
public final class TestPhraseFieldEnum extends Expr {
    @NepoField(name = "TYPE")
    public final Type type;

    @NepoField(name = "TYPE1")
    public final Type type1;

    public TestPhraseFieldEnum(
        BlocklyProperties properties,

        Type type, Type type1) {
        super(properties);
        this.type = type;
        this.type1 = type1;
    }

    public enum Type {
        RESET, SET
    }
}
