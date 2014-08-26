package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Expr} so they can be used as {@link Stmt}.
 */
public class ExprStmt<V> extends Stmt<V> {
    private final Expr<V> expr;

    private ExprStmt(Expr<V> expr) {
        super(Phrase.Kind.EXPR_STMT);
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitExprStmt(this);
    }

}
