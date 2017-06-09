package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class EmptyExprTest {

    @Test
    public void make() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(BlocklyType.NUMBER_INT);
        String a = "EmptyExpr [defVal=NUMBER_INT]";
        Assert.assertEquals(a, emptyExpr.toString());
    }

    @Test
    public void getDefVal() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(BlocklyType.NUMBER_INT);
        Assert.assertEquals(BlocklyType.NUMBER_INT, emptyExpr.getDefVal());
    }

    @Test
    public void getPrecedence() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(BlocklyType.NUMBER_INT);
        Assert.assertEquals(999, emptyExpr.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(BlocklyType.NUMBER_INT);
        Assert.assertEquals(Assoc.NONE, emptyExpr.getAssoc());
    }
}
