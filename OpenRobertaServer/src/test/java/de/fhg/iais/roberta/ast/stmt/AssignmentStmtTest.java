package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.Var.TypeVar;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;

public class AssignmentStmtTest {

    @Test
    public void make() throws Exception {
        Var<Void> var = Var.make("item", TypeVar.INTEGER, false, "");
        NumConst<Void> numConst = NumConst.make("0", false, "");
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, false, "");

        String a = "\nVar [item] := NumConst [0]\n";
        Assert.assertEquals(a, assignStmt.toString());
    }

    @Test
    public void getName() throws Exception {
        Var<Void> var = Var.make("item", TypeVar.INTEGER, false, "");
        NumConst<Void> numConst = NumConst.make("0", false, "");
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, false, "");

        Assert.assertEquals("Var [item]", assignStmt.getName().toString());
    }

    @Test
    public void getExpr() throws Exception {
        Var<Void> var = Var.make("item", TypeVar.INTEGER, false, "");
        NumConst<Void> numConst = NumConst.make("0", false, "");
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, false, "");

        Assert.assertEquals(numConst.toString(), assignStmt.getExpr().toString());
    }

}
