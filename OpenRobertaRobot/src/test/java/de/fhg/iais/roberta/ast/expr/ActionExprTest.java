package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;

public class ActionExprTest {

    @Test
    public void make() throws Exception {
        MotorDriveStopAction<Void> sa = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1"), null);
        ActionExpr<Void> ae = ActionExpr.make(sa);

        String a = "ActionExpr [StopAction []]";

        Assert.assertEquals(a, ae.toString());
    }

    @Test
    public void getAction() throws Exception {
        MotorDriveStopAction<Void> sa = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1"), null);
        ActionExpr<Void> ae = ActionExpr.make(sa);

        Assert.assertEquals(sa.toString(), ae.getAction().toString());
    }

    @Test
    public void getPrecedence() throws Exception {
        MotorDriveStopAction<Void> sa = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1"), null);
        ActionExpr<Void> ae = ActionExpr.make(sa);

        Assert.assertEquals(999, ae.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        MotorDriveStopAction<Void> sa = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1"), null);
        ActionExpr<Void> ae = ActionExpr.make(sa);

        Assert.assertEquals(Assoc.NONE, ae.getAssoc());
    }

}
