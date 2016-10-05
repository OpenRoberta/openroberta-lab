package de.fhg.iais.roberta.syntax.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robControls_wait_time</b> block from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate wait statement.<br/>
 */
public class WaitTimeStmt<V> extends Stmt<V> {
    private final Expr<V> time;

    private WaitTimeStmt(Expr<V> time, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WAIT_TIME"),properties, comment);
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
        return new WaitTimeStmt<>(time, properties, comment);
    }

    /**
     * @return the time
     */
    public Expr<V> getTime() {
        return this.time;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitWaitTimeStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.WAIT, Integer.class));
        return WaitTimeStmt.make(helper.convertPhraseToExpr(expr), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.WAIT, getTime());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "WaitTimeStmt [time=" + this.time + "]";
    }

}
