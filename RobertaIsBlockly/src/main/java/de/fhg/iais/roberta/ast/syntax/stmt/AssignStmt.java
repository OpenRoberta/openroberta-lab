package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.dbc.Assert;

public class AssignStmt extends Stmt {
    private final Var name;
    private final Expr expr;

    private AssignStmt(Var name, Expr expr) {
        super(Phrase.Kind.AssignStmt);
        Assert.isTrue(name.isReadOnly() && expr.isReadOnly());
        this.name = name;
        this.expr = expr;
        setReadOnly();
    }

    public static AssignStmt make(Var name, Expr expr) {
        return new AssignStmt(name, expr);
    }

    public final Var getName() {
        return this.name;
    }

    public final Expr getExpr() {
        return this.expr;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, null);
        sb.append(this.name).append(" := ").append(this.expr).append("\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }

}
