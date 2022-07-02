package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robControls_wait_time"}, name = "WAIT_TIME")
public final class WaitTimeStmt<V> extends Stmt<V> {
    @NepoValue(name = BlocklyConstants.WAIT, type = BlocklyType.NUMBER_INT)
    public final Expr<V> time;

    public WaitTimeStmt(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> time) {
        super(properties, comment);
        Assert.isTrue(time != null && time.isReadOnly());
        this.time = time;
        setReadOnly();
    }

}
