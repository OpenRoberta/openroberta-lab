package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_FIELD_BOOLEAN")
public class TestPhraseFieldBoolean<V> extends Expr<V> {
    @NepoField(name = "FLAG")
    public final boolean flag;

    public TestPhraseFieldBoolean(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, boolean flag) {
        super(kind, properties, comment);
        this.flag = flag;
    }
}
