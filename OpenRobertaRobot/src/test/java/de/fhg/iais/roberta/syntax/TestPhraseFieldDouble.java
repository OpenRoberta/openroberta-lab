package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "TEST_PHRASE_FIELD_DOUBLE", blocklyNames = {"test_phrase_field_double"}, category = "EXPR")
public class TestPhraseFieldDouble<V> extends Expr<V> {
    @NepoField(name = "VALUE")
    public final double value;

    public TestPhraseFieldDouble(BlocklyBlockProperties properties, BlocklyComment comment, double value) {
        super(properties, comment);
        this.value = value;
    }
}
