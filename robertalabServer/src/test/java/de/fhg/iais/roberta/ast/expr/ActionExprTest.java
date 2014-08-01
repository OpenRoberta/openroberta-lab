package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.StopAction;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;

public class ActionExprTest {

    @Test
    public void make() throws Exception {
        StopAction sa = StopAction.make();
        ActionExpr ae = ActionExpr.make(sa);

        String a = "ActionExpr [StopAction []]";

        Assert.assertEquals(a, ae.toString());
    }

    @Test
    public void getAction() throws Exception {
        StopAction sa = StopAction.make();
        ActionExpr ae = ActionExpr.make(sa);

        Assert.assertEquals(sa.toString(), ae.getAction().toString());
    }

    @Test
    public void getPrecedence() throws Exception {
        StopAction sa = StopAction.make();
        ActionExpr ae = ActionExpr.make(sa);

        Assert.assertEquals(999, ae.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        StopAction sa = StopAction.make();
        ActionExpr ae = ActionExpr.make(sa);

        Assert.assertEquals(Assoc.NONE, ae.getAssoc());
    }

}
