package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;

public class AssignmentStmtTest {

    @Test
    public void make() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMERIC, "item", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);

        String a = "\nVar [item] := NumConst [0]\n";
        Assert.assertEquals(a, assignStmt.toString());
    }

    @Test
    public void getName() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMERIC, "item", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);

        Assert.assertEquals("Var [item]", assignStmt.getName().toString());
    }

    @Test
    public void getExpr() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMERIC, "item", BlocklyBlockProperties.make("1", "1", true, false, false, false, false, true), null);
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", true, false, false, false, false, true), null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, BlocklyBlockProperties.make("1", "1", true, false, false, false, false, true), null);

        Assert.assertEquals(numConst.toString(), assignStmt.getExpr().toString());
    }

}
