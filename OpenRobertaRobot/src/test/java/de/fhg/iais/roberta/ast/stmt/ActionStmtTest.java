package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;

public class ActionStmtTest {

    @Test
    public void make() throws Exception {
        MotorDriveStopAction<Void> action = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1"), null);
        ActionStmt<Void> actionStmt = ActionStmt.make(action);

        String a = "\nAktionStmt [StopAction []]";
        Assert.assertEquals(a, actionStmt.toString());
    }

    @Test
    public void getAction() throws Exception {
        MotorDriveStopAction<Void> action = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1"), null);
        ActionStmt<Void> actionStmt = ActionStmt.make(action);

        Assert.assertEquals("StopAction []", actionStmt.getAction().toString());
    }

}
