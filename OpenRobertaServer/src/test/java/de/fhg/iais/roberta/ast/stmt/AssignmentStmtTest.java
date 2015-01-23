package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;

public class AssignmentStmtTest {

    @Test
    public void make() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMERIC, "item", null, null);
        NumConst<Void> numConst = NumConst.make("0", null, null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, null, null);

        String a = "\nVar [item] := NumConst [0]\n";
        Assert.assertEquals(a, assignStmt.toString());
    }

    @Test
    public void getName() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMERIC, "item", null, null);
        NumConst<Void> numConst = NumConst.make("0", null, null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, null, null);

        Assert.assertEquals("Var [item]", assignStmt.getName().toString());
    }

    @Test
    public void getExpr() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMERIC, "item", null, null);
        NumConst<Void> numConst = NumConst.make("0", null, null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, null, null);

        Assert.assertEquals(numConst.toString(), assignStmt.getExpr().toString());
    }

}
