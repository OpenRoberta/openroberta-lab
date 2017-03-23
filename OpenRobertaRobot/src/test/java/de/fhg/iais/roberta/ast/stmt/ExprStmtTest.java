package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.util.test.Helper;

public class ExprStmtTest {
    Helper h = new Helper();

    @Test
    public void make() throws Exception {
        NumConst<Void> expr = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        ExprStmt<Void> exprStmt = ExprStmt.make(expr);

        String a = "\nexprStmt NumConst [0]";
        Assert.assertEquals(a, exprStmt.toString());
    }

    @Test
    public void getExpr() throws Exception {
        NumConst<Void> expr = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        ExprStmt<Void> exprStmt = ExprStmt.make(expr);

        Assert.assertEquals("NumConst [0]", exprStmt.getExpr().toString());
    }

}
