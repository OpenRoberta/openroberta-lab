package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>robControls_wait_time</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate wait
 * statement.<br/>
 */
public class WaitTimeStmt<V> extends Stmt<V> {
    private final Expr<V> time;

    private WaitTimeStmt(Expr<V> time, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WAIT_TIME"), properties, comment);
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitWaitTimeStmt(this);
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
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.WAIT, BlocklyType.NUMBER_INT));
        return WaitTimeStmt.make(helper.convertPhraseToExpr(expr), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.WAIT, getTime());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "WaitTimeStmt [time=" + this.time + "]";
    }

}
