package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;

public class StmtTest {
    Expr expr;
    StmtList l1;
    StmtList l2;
    Stmt stmt;

    @Test
    public void test() {
        //        Expr expr = NumConst.make("1");
        //        StmtList l1 = StmtList.make();
        //        l1.addStmt(ExprStmt.make(expr));
        //        l1.setReadOnly();
        //        StmtList l2 = StmtList.make();
        //        l2.addStmt(ExprStmt.make(expr));
        //        l2.addStmt(ExprStmt.make(expr));
        //        l2.setReadOnly();
        //        Stmt stmt = IfStmt.make(expr, l1, l2);
        //        IfStmt ifStmt = stmt.getAs(IfStmt.class); // the user is responsible for the match IfStmt <--> IfStmt.class
        //        System.out.println(ifStmt);
        //        try {
        //            stmt.getAs(RepeatStmt.class);
        //            Assert.fail();
        //        } catch ( Exception e ) {
        //            System.out.println("expected");
        //        }
        //        switch ( stmt.getKind() ) {
        //            case IfStmt:
        //                IfStmt t1 = stmt.getAs(IfStmt.class);
        //                System.out.println(t1.getExpr());
        //                break;
        //            case RepeatStmt:
        //                RepeatStmt t2 = stmt.getAs(RepeatStmt.class);
        //                System.out.println(t2.getExpr());
        //                break;
        //
        //            default:
        //                Assert.fail("unknown kind of statement");
        //        }
    }
}
