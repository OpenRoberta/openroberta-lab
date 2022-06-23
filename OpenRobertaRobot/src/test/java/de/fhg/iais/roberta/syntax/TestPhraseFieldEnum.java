package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "TEST_PHRASE_FIELD_ENUM", blocklyNames = {"test_phrase_field_enum"}, category = "EXPR")
public class TestPhraseFieldEnum<V> extends Expr<V> {
    @NepoField(name = "TYPE")
    public final Type type;

    @NepoField(name = "TYPE1")
    public final Type type1;

    public TestPhraseFieldEnum(
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Type type, Type type1) {
        super(properties, comment);
        this.type = type;
        this.type1 = type1;
    }

    public enum Type {
        RESET, SET
    }
}
