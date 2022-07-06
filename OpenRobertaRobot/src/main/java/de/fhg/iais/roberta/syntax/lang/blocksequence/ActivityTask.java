package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "TASK", blocklyNames = {"robControls_activity"}, name = "ACTIVITY_TASK")
public final class ActivityTask extends Task {
    @NepoValue(name = BlocklyConstants.ACTIVITY, type = BlocklyType.STRING)
    public final Expr activityName;

    public ActivityTask(BlocklyProperties properties, Expr activityName) {
        super(properties);
        Assert.isTrue(activityName.isReadOnly() && activityName != null);
        this.activityName = activityName;
        setReadOnly();
    }

}
