package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.test.GeneralTestsHelper;

public class AssignmentStmtTest {

    @Before
    public void loadBlocks() {
        GeneralTestsHelper.loadBlocks("classpath:/robotCommon.yml");
    }

    @Test
    public void make() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMBER, "item", BlocklyBlockProperties.make("1", "1"), null);
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, BlocklyBlockProperties.make("1", "1"), null);

        String a = "\nVar [item] := NumConst [0]\n";
        Assert.assertEquals(a, assignStmt.toString());
    }

    @Test
    public void getName() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMBER, "item", BlocklyBlockProperties.make("1", "1"), null);
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        AssignStmt<Void> assignStmt = AssignStmt.make(var, numConst, BlocklyBlockProperties.make("1", "1"), null);

        Assert.assertEquals("Var [item]", assignStmt.getName().toString());
    }

    @Test
    public void getExpr() throws Exception {
        Var<Void> var = Var.make(BlocklyType.NUMBER, "item", BlocklyBlockProperties.make("1", "1", true, false, false, false, false, true, false, false), null);
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", true, false, false, false, false, true, false, false), null);
        AssignStmt<Void> assignStmt =
            AssignStmt.make(var, numConst, BlocklyBlockProperties.make("1", "1", true, false, false, false, false, true, false, false), null);

        Assert.assertEquals(numConst.toString(), assignStmt.getExpr().toString());
    }

}
