package de.fhg.iais.roberta.syntax.action.serial;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "WRITE_TO_SERIAL")
public class SerialWriteAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.ANY)
    public final Expr<V> value;

    public SerialWriteAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> value) {
        super(kind, properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    public static <V> SerialWriteAction<V> make(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SerialWriteAction<>(BlockTypeContainer.getByName("WRITE_TO_SERIAL"), properties, comment, value);
    }

    public Expr<V> getValue() {
        return this.value;
    }

}