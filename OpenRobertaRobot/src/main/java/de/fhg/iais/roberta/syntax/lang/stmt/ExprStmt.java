package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Expr} so they can be used as {@link Stmt}.
 */
@NepoBasic(name = "EXPR_STMT", category = "STMT", blocklyNames = {})
public final class ExprStmt<V> extends Stmt<V> {
    public final Expr<V> expr;

    public ExprStmt(Expr<V> expr) {
        super(expr.getProperty());
        Assert.isTrue(expr.isReadOnly());
        this.expr = expr;
        setReadOnly();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        sb.append("exprStmt ").append(this.expr);
        return sb.toString();
    }

    @Override
    public Block astToBlock() {
        return this.expr.astToBlock();
    }
}
