package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "TASK", blocklyNames = {"robControls_activity"}, name = "ACTIVITY_TASK")
public final class ActivityTask<V> extends Task<V> {
    @NepoValue(name = BlocklyConstants.ACTIVITY, type = BlocklyType.STRING)
    public final Expr<V> activityName;

    public ActivityTask(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> activityName) {
        super(properties, comment);
        Assert.isTrue(activityName.isReadOnly() && activityName != null);
        this.activityName = activityName;
        setReadOnly();
    }

}
