package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;

public class ActionStmtTest {

    @Test
    public void make() throws Exception {
        MotorDriveStopAction action = MotorDriveStopAction.make();
        ActionStmt actionStmt = ActionStmt.make(action);

        String a = "\nAktionStmt [StopAction []]";
        Assert.assertEquals(a, actionStmt.toString());
    }

    @Test
    public void getAction() throws Exception {
        MotorDriveStopAction action = MotorDriveStopAction.make();
        ActionStmt actionStmt = ActionStmt.make(action);

        Assert.assertEquals("StopAction []", actionStmt.getAction().toString());
    }

}
