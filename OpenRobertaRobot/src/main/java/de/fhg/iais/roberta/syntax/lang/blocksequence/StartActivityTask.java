package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"robControls_start_activity"}, name = "START_ACTIVITY_TASK", blocklyType = BlocklyType.NULL)
public final class StartActivityTask extends Expr {
    @NepoValue(name = BlocklyConstants.ACTIVITY, type = BlocklyType.STRING)
    public Expr activityName;

    public StartActivityTask(BlocklyProperties properties, Expr activityName) {
        super(properties);
        this.activityName = activityName;
        setReadOnly();
    }

}
