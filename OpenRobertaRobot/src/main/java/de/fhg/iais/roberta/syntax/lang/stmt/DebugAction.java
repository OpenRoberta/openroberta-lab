package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(containerType = "DEBUG_STMT")
public class DebugAction<V> extends Stmt<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.ANY)
    public final Expr<V> value;

    public DebugAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> value) {
        super(kind, properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    public static <V> DebugAction<V> make(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DebugAction<>(BlockTypeContainer.getByName("DEBUG_STMT"), properties, comment, value);
    }

    public Expr<V> getValue() {
        return this.value;
    }

}