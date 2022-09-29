package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_FIELD", blocklyNames = {"test_phrase_field"}, category = "EXPR")
public final class TestPhraseField extends Expr {
    @NepoField(name = "TYPE", value = "DEFAULT")
    public final String type;

    @NepoField(name = "FLAG", value = "true")
    public final boolean flag;

    @NepoField(name = "VALUE", value = "2.15")
    public final double value;

    @NepoField(name = "OP_TYPE", value = "SET")
    public final OperationType operationType;

    public TestPhraseField(
        BlocklyProperties properties,

        String type,
        boolean flag,
        double value,
        OperationType operationType) {
        super(properties);
        this.type = type;
        this.flag = flag;
        this.value = value;
        this.operationType = operationType;
    }

    public enum OperationType {
        SET, RESET,
    }
}
