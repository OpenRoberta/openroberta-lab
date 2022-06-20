package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_FIELD_BOOLEAN", blocklyNames = {"test_phrase_field_boolean"}, category = "EXPR")
public class TestPhraseFieldBoolean<V> extends Expr<V> {
    @NepoField(name = "FLAG")
    public final boolean flag;

    public TestPhraseFieldBoolean(BlocklyBlockProperties properties, BlocklyComment comment, boolean flag) {
        super(properties, comment);
        this.flag = flag;
    }
}
