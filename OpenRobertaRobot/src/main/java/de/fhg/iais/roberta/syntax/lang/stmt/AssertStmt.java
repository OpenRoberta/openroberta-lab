package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_assert"}, name = "ASSERT_STMT")
public final class AssertStmt<V> extends Stmt<V> {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.BOOLEAN)
    public final Expr<V> asserts;
    @NepoField(name = BlocklyConstants.TEXT)
    public final String msg;

    public AssertStmt(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> asserts, String msg) {
        super(properties, comment);
        Assert.isTrue(asserts != null && asserts.isReadOnly());
        this.asserts = asserts;
        this.msg = msg;
        setReadOnly();
    }

    public static <V> AssertStmt<V> make(Expr<V> asserts, String msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AssertStmt<>(properties, comment, asserts, msg);
    }

    public Expr<V> getAssert() {
        return this.asserts;
    }

    public String getMsg() {
        return this.msg;
    }

}
