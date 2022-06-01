package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.*;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robControls_activity</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * new thread.<br/>
 * <br/>
 */
@NepoOp(containerType = "ACTIVITY_TASK")
public class ActivityTask<V> extends Task<V> {
    @NepoValue(name = BlocklyConstants.ACTIVITY, type = BlocklyType.STRING)
    public Expr<V> activityName;

    public ActivityTask(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> activityName) {
        super(kind, properties, comment);
        Assert.isTrue(activityName.isReadOnly() && activityName != null);
        this.activityName = activityName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ActivityTask}. This instance is read only and can not be modified.
     *
     * @param activityName name of the new thread; must be <b>non</b> null and <b>read-only</b>
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ActivityTask}
     */
    public static <V> ActivityTask<V> make(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ActivityTask<>(BlockTypeContainer.getByName("ACTIVITY_TASK"), properties, comment, activityName);
    }

    /**
     * @return name of the thread
     */
    public Expr<V> getActivityName() {
        return this.activityName;
    }

}
