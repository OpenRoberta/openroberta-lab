package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;

public class EmptyExprTest {

    @Test
    public void make() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(Integer.class);
        String a = "EmptyExpr [defVal=class java.lang.Integer]";
        Assert.assertEquals(a, emptyExpr.toString());
    }

    @Test
    public void getDefVal() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(Integer.class);
        Assert.assertEquals(Integer.class, emptyExpr.getDefVal());
    }

    @Test
    public void getPrecedence() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(Integer.class);
        Assert.assertEquals(999, emptyExpr.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(Integer.class);
        Assert.assertEquals(Assoc.NONE, emptyExpr.getAssoc());
    }
}
