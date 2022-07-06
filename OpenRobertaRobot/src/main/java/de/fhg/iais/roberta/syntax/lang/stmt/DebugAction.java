package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_debug"}, name = "DEBUG_STMT")
public final class DebugAction extends Stmt {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.ANY)
    public final Expr value;

    public DebugAction(BlocklyProperties properties, Expr value) {
        super(properties);
        Assert.notNull(value);
        this.value = value;
        setReadOnly();
    }

}