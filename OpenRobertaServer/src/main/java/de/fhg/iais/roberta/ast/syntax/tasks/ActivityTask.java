package de.fhg.iais.roberta.ast.syntax.tasks;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robControls_activity</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class will generate
 * code for new thread.<br/>
 * <br/>
 */
public class ActivityTask<V> extends Task<V> {
    private final Expr<V> activityName;

    private ActivityTask(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.ACTIVITY_TASK, properties, comment);
        Assert.isTrue(activityName.isReadOnly() && activityName != null);
        this.activityName = activityName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ActivityTask}. This instance is read only and
     * can not be modified.
     *
     * @param activityName name of the new thread; must be <b>non</b> null and <b>read-only</b>
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ActivityTask}
     */
    public static <V> ActivityTask<V> make(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ActivityTask<V>(activityName, properties, comment);
    }

    /**
     * @return name of the thread
     */
    public Expr<V> getActivityName() {
        return this.activityName;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitActivityTask(this);
    }

    @Override
    public String toString() {
        return "ActivityTask [activityName=" + this.activityName + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ACTIVITY, getActivityName());
        return jaxbDestination;
    }
}
