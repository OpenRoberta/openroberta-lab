package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class ActionStmtTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void make() throws Exception {
        MotorDriveStopAction<Void> action =
            MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        ActionStmt<Void> actionStmt = ActionStmt.make(action);

        String a = "\nAktionStmt [StopAction []]";
        Assert.assertEquals(a, actionStmt.toString());
    }

    @Test
    public void getAction() throws Exception {
        MotorDriveStopAction<Void> action =
            MotorDriveStopAction.make(BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        ActionStmt<Void> actionStmt = ActionStmt.make(action);

        Assert.assertEquals("StopAction []", actionStmt.getAction().toString());
    }

}
