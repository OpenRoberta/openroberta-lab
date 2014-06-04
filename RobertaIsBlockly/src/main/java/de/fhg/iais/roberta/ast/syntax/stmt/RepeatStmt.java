package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

public class RepeatStmt extends Stmt {
    private final Mode mode;
    private final Expr expr;
    private final StmtList list;

    private RepeatStmt(Mode mode, Expr expr, StmtList list) {
        Assert.isTrue(expr.isReadOnly() && list.isReadOnly());
        this.expr = expr;
        this.list = list;
        this.mode = mode;
        setReadOnly();
    }

    public static RepeatStmt make(Mode mode, Expr expr, StmtList list) {
        return new RepeatStmt(mode, expr, list);
    }

    public Mode getMode() {
        return this.mode;
    }

    public final Expr getExpr() {
        return this.expr;
    }

    public final StmtList getlist() {
        return this.list;
    }

    @Override
    public Kind getKind() {
        return Kind.Repeat;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        int next = indentation + 3;
        appendNewLine(sb, indentation, null);
        sb.append("(repeat ").append(this.expr);
        this.list.toStringBuilder(sb, next);
        appendNewLine(sb, indentation, ")");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }

    public static enum Mode {
        WHILE, UNTIL, TIMES, FOR, FOR_EACH;
    }

}
