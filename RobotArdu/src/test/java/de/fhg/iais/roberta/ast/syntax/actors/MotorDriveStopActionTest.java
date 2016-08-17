package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MotorDriveStopActionTest {

    @Test
    public void stop() throws Exception {
        final String a = "\none.stop();";

        Helper.assertCodeIsOk(a, "/ast/actions/action_Stop.xml");
    }
}