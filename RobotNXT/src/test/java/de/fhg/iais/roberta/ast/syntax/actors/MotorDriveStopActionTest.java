package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MotorDriveStopActionTest {
    Helper h = new Helper();

    @Test
    public void stop() throws Exception {
        final String a = "\nOff(OUT_BC);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Stop.xml");
    }
}