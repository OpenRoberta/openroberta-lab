package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robControls_wait_time</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate wait
 * statement.<br/>
 */
@NepoPhrase(category = "STMT", blocklyNames = {"robControls_wait_time"}, containerType = "WAIT_TIME")
public final class WaitTimeStmt<V> extends Stmt<V> {
    @NepoValue(name = BlocklyConstants.WAIT, type = BlocklyType.NUMBER_INT)
    public final Expr<V> time;

    public WaitTimeStmt(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> time) {
        super(properties, comment);
        Assert.isTrue(time != null && time.isReadOnly());
        this.time = time;
        setReadOnly();
    }

    /**
     * Create read only object of type {@link WaitTimeStmt}
     *
     * @param time; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment for the block,
     * @return
     */
    public static <V> WaitTimeStmt<V> make(Expr<V> time, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WaitTimeStmt<>(properties, comment, time);
    }

    /**
     * @return the time
     */
    public Expr<V> getTime() {
        return this.time;
    }

}
