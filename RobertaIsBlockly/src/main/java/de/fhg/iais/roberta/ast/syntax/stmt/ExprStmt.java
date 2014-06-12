package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

public class ExprStmt extends Stmt {
    private final Expr expr;

    private ExprStmt(Expr expr) {
        super(Phrase.Kind.EXPR_STMT);
        Assert.isTrue(expr.isReadOnly());
        this.expr = expr;
        setReadOnly();
    }

    public static ExprStmt make(Expr expr) {
        return new ExprStmt(expr);
    }

    public final Expr getExpr() {
        return expr;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append("exprStmt ").append(expr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }

}
