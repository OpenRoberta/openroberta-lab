package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;

public class ActionStmtTest {

    @Test
    public void make() throws Exception {
        MotorDriveStopAction<Void> action = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        ActionStmt<Void> actionStmt = ActionStmt.make(action);

        String a = "\nAktionStmt [StopAction []]";
        Assert.assertEquals(a, actionStmt.toString());
    }

    @Test
    public void getAction() throws Exception {
        MotorDriveStopAction<Void> action = MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        ActionStmt<Void> actionStmt = ActionStmt.make(action);

        Assert.assertEquals("StopAction []", actionStmt.getAction().toString());
    }

}
