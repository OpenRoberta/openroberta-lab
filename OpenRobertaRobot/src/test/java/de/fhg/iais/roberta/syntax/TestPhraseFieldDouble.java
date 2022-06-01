package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_FIELD_DOUBLE")
public class TestPhraseFieldDouble<V> extends Expr<V> {
    @NepoField(name = "VALUE")
    public final double value;

    public TestPhraseFieldDouble(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, double value) {
        super(kind, properties, comment);
        this.value = value;
    }
}
