package de.fhg.iais.roberta.syntax.lang.blocksequence;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>robControls_activity</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * new thread.<br/>
 * <br/>
 */
public class ActivityTask<V> extends Task<V> {
    private final Expr<V> activityName;

    private ActivityTask(Expr<V> activityName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ACTIVITY_TASK"), properties, comment);
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitActivityTask(this);
    }

    @Override
    public String toString() {
        return "ActivityTask [activityName=" + this.activityName + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.ACTIVITY, BlocklyType.STRING));
        return ActivityTask.make(helper.convertPhraseToExpr(expr), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ACTIVITY, getActivityName());
        return jaxbDestination;
    }
}
