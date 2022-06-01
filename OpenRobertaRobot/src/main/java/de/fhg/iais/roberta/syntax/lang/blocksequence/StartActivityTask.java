package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * This class represents the <b>robControls_start_activity</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for starting a thread.<br/>
 * <br/>
 */
@NepoOp(containerType = "START_ACTIVITY_TASK", blocklyType = BlocklyType.NULL)
public class StartActivityTask<V> extends Expr<V> {
    @NepoValue(name = BlocklyConstants.ACTIVITY, type = BlocklyType.STRING)
    public Expr<V> activityName;

    public StartActivityTask(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> activityName) {
        super(kind, properties, comment);
        this.activityName = activityName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link StartActivityTask}. This instance is read only and can not be modified.
     *
     * @param activityName name of the new thread; must be <b>non</b> null and <b>read-only</b>
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StartActivityTask}
     */
    public static <V> StartActivityTask<V> make(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StartActivityTask<V>(BlockTypeContainer.getByName("START_ACTIVITY_TASK"), properties, comment, activityName);
    }

    /**
     * @return name of the thread
     */
    public Expr<V> getActivityName() {
        return this.activityName;
    }

}
