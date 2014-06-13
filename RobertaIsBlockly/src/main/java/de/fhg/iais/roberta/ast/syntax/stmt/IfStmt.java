package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;

public class IfStmt extends Stmt {
    private final List<Expr> expr;
    private final List<StmtList> thenList;
    private final StmtList elseList;

    private IfStmt(List<Expr> expr, List<StmtList> thenList, StmtList elseList) {
        super(Phrase.Kind.IfStmt);
        // Assert.isTrue(expr.isReadOnly() && thenList.isReadOnly() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        setReadOnly();
    }

    public static IfStmt make(List<Expr> expr, List<StmtList> thenList, StmtList elseList) {
        return new IfStmt(expr, thenList, elseList);
    }

    public static IfStmt make(List<Expr> expr, List<StmtList> thenList) {
        StmtList elseList = StmtList.make();
        elseList.setReadOnly();
        return new IfStmt(expr, thenList, elseList);
    }

    public static IfStmt make(List<Expr> expr, List<StmtList> thenList, IfStmt elseIf) {
        StmtList elseList = StmtList.make();
        elseList.addStmt(elseIf);
        elseList.setReadOnly();
        return new IfStmt(expr, thenList, elseList);
    }

    public final List<Expr> getExpr() {
        return this.expr;
    }

    public final List<StmtList> getThenList() {
        return this.thenList;
    }

    public final StmtList getElseList() {
        return this.elseList;
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        int next = indentation + 3;
        appendNewLine(sb, indentation, null);
        for ( int i = 0; i < this.expr.size(); i++ ) {
            sb.append("(if ").append(this.expr.get(i));
            appendNewLine(sb, indentation, ",then");
            this.thenList.get(i).toStringBuilder(sb, next);
            if ( i + 1 < this.expr.size() ) {
                appendNewLine(sb, indentation, " else ");
            }
            //            if ( this.elseList.get().size() != 0 ) {
            //                appendNewLine(sb, indentation, ",else");
            //                this.elseList.toStringBuilder(sb, next);
            //            }
        }
        appendNewLine(sb, indentation, ")");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }
}
