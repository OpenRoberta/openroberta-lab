package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_FIELD_ENUM")
public class TestPhraseFieldEnum<V> extends Expr<V> {
    @NepoField(name = "TYPE")
    public final Type type;

    @NepoField(name = "TYPE1")
    public final Type type1;

    public TestPhraseFieldEnum(
        BlockType kind,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Type type, Type type1) {
        super(kind, properties, comment);
        this.type = type;
        this.type1 = type1;
    }

    public enum Type {
        RESET, SET
    }
}
