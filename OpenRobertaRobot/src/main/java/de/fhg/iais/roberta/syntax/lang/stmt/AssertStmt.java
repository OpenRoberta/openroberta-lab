package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robControls_wait_time</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate wait
 * statement.<br/>
 */
@NepoPhrase(containerType = "ASSERT_STMT")
public class AssertStmt<V> extends Stmt<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.BOOLEAN)
    public final Expr<V> asserts;
    @NepoField(name = BlocklyConstants.TEXT)
    public final String msg;

    public AssertStmt(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> asserts, String msg) {
        super(kind, properties, comment);
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
        return new AssertStmt<>(BlockTypeContainer.getByName("ASSERT_STMT"), properties, comment, asserts, msg);
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

}
