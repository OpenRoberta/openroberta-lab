package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Expr} so they can be used as {@link Stmt}.
 */
@NepoBasic(containerType = "EXPR_STMT", category = "STMT", blocklyNames = {})
public final class ExprStmt<V> extends Stmt<V> {
    public final Expr<V> expr;

    private ExprStmt(Expr<V> expr) {
        super(expr.getProperty(), expr.getComment());
        Assert.isTrue(expr.isReadOnly());
        this.expr = expr;
        setReadOnly();
    }

    /**
     * Create object of the class {@link ExprStmt}.
     *
     * @param expr that we want to wrap
     * @return statement with wrapped expression inside
     */
    public static <V> ExprStmt<V> make(Expr<V> expr) {
        return new ExprStmt<V>(expr);
    }

    /**
     * @return expression that is wrapped in the statement
     */
    public final Expr<V> getExpr() {
        return this.expr;
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
        return getExpr().astToBlock();
    }
}
