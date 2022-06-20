package de.fhg.iais.roberta.syntax.action.serial;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_serial_print"}, containerType = "WRITE_TO_SERIAL")
public final class SerialWriteAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.ANY)
    public final Expr<V> value;

    public SerialWriteAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> value) {
        super(properties, comment);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

    public static <V> SerialWriteAction<V> make(Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SerialWriteAction<>(properties, comment, value);
    }

    public Expr<V> getValue() {
        return this.value;
    }

}