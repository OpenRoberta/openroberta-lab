package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
public class AssertStmt<V> extends Stmt<V> {
    private final Expr<V> asserts;
    private final String msg;

    private AssertStmt(Expr<V> asserts, String msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ASSERT_STMT"), properties, comment);
        Assert.isTrue(asserts != null && asserts.isReadOnly());
        this.asserts = asserts;
        this.msg = msg;
        setReadOnly();
    }

    /**
     * Create read only object of type {@link AssertStmt}
     *
     * @param time; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment for the block,
     * @return
     */
    public static <V> AssertStmt<V> make(Expr<V> asserts, String msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AssertStmt<>(asserts, msg, properties, comment);
    }

    /**
     * @return what
     */
    public Expr<V> getAssert() {
        return this.asserts;
    }

    /**
     * @return message
     */
    public String getMsg() {
        return this.msg;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitAssertStmt(this);
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
        List<Field> fields = helper.extractFields(block, (short) 1);
        Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.BOOLEAN));
        String msg = helper.extractField(fields, BlocklyConstants.TEXT);
        return AssertStmt.make(helper.convertPhraseToExpr(expr), msg, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.OUT, getAssert());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TEXT, getMsg());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "AssertStmt [asserts " + this.msg + ": " + this.asserts + "]";
    }

}
