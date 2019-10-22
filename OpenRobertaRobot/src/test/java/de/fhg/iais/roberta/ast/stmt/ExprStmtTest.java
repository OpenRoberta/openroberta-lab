package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;

public class ExprStmtTest {

    @Test
    public void make() throws Exception {
        NumConst<Void> expr = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        ExprStmt<Void> exprStmt = ExprStmt.make(expr);

        String a = "\nexprStmt NumConst [0]";
        Assert.assertEquals(a, exprStmt.toString());
    }

    @Test
    public void getExpr() throws Exception {
        NumConst<Void> expr = NumConst.make("0", BlocklyBlockProperties.make("1", "1"), null);
        ExprStmt<Void> exprStmt = ExprStmt.make(expr);

        Assert.assertEquals("NumConst [0]", exprStmt.getExpr().toString());
    }

}
