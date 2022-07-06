package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robControls_wait_time"}, name = "WAIT_TIME")
public final class WaitTimeStmt extends Stmt {
    @NepoValue(name = BlocklyConstants.WAIT, type = BlocklyType.NUMBER_INT)
    public final Expr time;

    public WaitTimeStmt(BlocklyProperties properties, Expr time) {
        super(properties);
        Assert.isTrue(time != null && time.isReadOnly());
        this.time = time;
        setReadOnly();
    }

}
