package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;

public class IfStmt extends Stmt {
    private final Expr expr;
    private final StmtList thenList;
    private final StmtList elseList;

    private IfStmt(Expr expr, StmtList thenList, StmtList elseList) {
        Assert.isTrue(expr.isReadOnly() && thenList.isReadOnly() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        setReadOnly();
    }

    public static IfStmt make(Expr expr, StmtList thenList, StmtList elseList) {
        return new IfStmt(expr, thenList, elseList);
    }

    public static IfStmt make(Expr expr, StmtList thenList) {
        StmtList elseList = StmtList.make();
        elseList.setReadOnly();
        return new IfStmt(expr, thenList, elseList);
    }

    public static IfStmt make(Expr expr, StmtList thenList, IfStmt elseIf) {
        StmtList elseList = StmtList.make();
        elseList.addStmt(elseIf);
        elseList.setReadOnly();
        return new IfStmt(expr, thenList, elseList);
    }

    public final Expr getExpr() {
        return this.expr;
    }

    public final StmtList getThenList() {
        return this.thenList;
    }

    public final StmtList getElseList() {
        return this.elseList;
    }

    @Override
    public Kind getKind() {
        return Kind.If;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        int next = indentation + 3;
        appendNewLine(sb, indentation, null);
        sb.append("(if ").append(this.expr);
        appendNewLine(sb, indentation, ",then");
        this.thenList.toStringBuilder(sb, next);
        appendNewLine(sb, indentation, ",else");
        this.elseList.toStringBuilder(sb, next);
        appendNewLine(sb, indentation, ")");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }
}
