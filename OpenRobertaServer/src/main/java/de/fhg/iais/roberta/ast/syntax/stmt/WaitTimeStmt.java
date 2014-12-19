package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robControls_wait_time</b> block from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate wait statement.<br/>
 */
public class WaitTimeStmt<V> extends Stmt<V> {
    private final Expr<V> time;

    private WaitTimeStmt(Expr<V> time, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.WAIT_TIME, properties, comment);
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

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.WAIT, getTime());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "WaitTimeStmt [time=" + this.time + "]";
    }

}
