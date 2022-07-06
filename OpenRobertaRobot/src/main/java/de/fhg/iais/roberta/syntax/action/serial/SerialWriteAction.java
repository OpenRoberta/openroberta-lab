package de.fhg.iais.roberta.syntax.action.serial;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_serial_print"}, name = "WRITE_TO_SERIAL")
public final class SerialWriteAction extends Action {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.ANY)
    public final Expr value;

    public SerialWriteAction(BlocklyProperties properties, Expr value) {
        super(properties);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

}