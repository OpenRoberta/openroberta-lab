package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Expr} so they can be used as {@link Stmt}.
 * 
 * @author kcvejoski
 */
public class ExprStmt extends Stmt {
    private final Expr expr;

    private ExprStmt(Expr expr) {
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
    public static ExprStmt make(Expr expr) {
        return new ExprStmt(expr);
    }

    /**
     * @return expression that is wrapped in the statement
     */
    public final Expr getExpr() {
        return this.expr;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append("exprStmt ").append(this.expr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        generateJava(sb, 0);
        return sb.toString();
    }

}
