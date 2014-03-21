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
}
