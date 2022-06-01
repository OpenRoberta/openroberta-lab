package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_FIELD")
public class TestPhraseField<V> extends Expr<V> {

    @NepoField(name = "TYPE", value = "DEFAULT")
    public final String type;

    @NepoField(name = "FLAG", value = "true")
    public final boolean flag;

    @NepoField(name = "VALUE", value = "2.15")
    public final double value;

    @NepoField(name = "OP_TYPE", value = "SET")
    public final OperationType operationType;

    public TestPhraseField(
        BlockType kind,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        String type,
        boolean flag,
        double value,
        OperationType operationType) {
        super(kind, properties, comment);
        this.type = type;
        this.flag = flag;
        this.value = value;
        this.operationType = operationType;
    }

    public enum OperationType {
        SET, RESET,
    }
}
