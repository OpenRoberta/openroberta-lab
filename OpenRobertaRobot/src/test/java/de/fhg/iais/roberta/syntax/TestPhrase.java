package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE")
public class TestPhrase<V> extends Phrase<V> {

    @NepoField(name = "TYPE")
    public final String type;
    @NepoValue(name = "VALUE", type = BlocklyType.ANY)
    public final Expr<V> value;

    public TestPhrase(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String type, Expr<V> value) {
        super(kind, property, comment);
        this.type = type;
        this.value = value;
    }

}
