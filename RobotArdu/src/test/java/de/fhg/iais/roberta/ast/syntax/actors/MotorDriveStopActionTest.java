package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class MotorDriveStopActionTest {
    Helper h = new Helper();

    @Test
    public void stop() throws Exception {
        final String a = "\none.stop();";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Stop.xml", false);
    }
}