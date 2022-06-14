package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_debug"}, containerType = "DEBUG_STMT")
public class DebugAction<V> extends Stmt<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.ANY)
    public final Expr<V> value;

    public DebugAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> value) {
        super(properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    public static <V> DebugAction<V> make(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DebugAction<>(properties, comment, value);
    }

    public Expr<V> getValue() {
        return this.value;
    }

}