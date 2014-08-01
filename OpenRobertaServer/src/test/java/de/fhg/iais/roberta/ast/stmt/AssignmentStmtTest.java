package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;

public class AssignmentStmtTest {

    @Test
    public void make() throws Exception {
        Var var = Var.make("item");
        NumConst numConst = NumConst.make("0");
        AssignStmt assignStmt = AssignStmt.make(var, numConst);

        String a = "\nVar [item] := NumConst [0]\n";
        Assert.assertEquals(a, assignStmt.toString());
    }

    @Test
    public void getName() throws Exception {
        Var var = Var.make("item");
        NumConst numConst = NumConst.make("0");
        AssignStmt assignStmt = AssignStmt.make(var, numConst);

        Assert.assertEquals("Var [item]", assignStmt.getName().toString());
    }

    @Test
    public void getExpr() throws Exception {
        Var var = Var.make("item");
        NumConst numConst = NumConst.make("0");
        AssignStmt assignStmt = AssignStmt.make(var, numConst);

        Assert.assertEquals(numConst.toString(), assignStmt.getExpr().toString());
    }

}
