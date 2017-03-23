package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class MotorStopActionTest {
    Helper h = new Helper();

    @Test
    public void stopMotor() throws Exception {
        final String a = "one.stop1m(2)\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorStop.xml", false);
    }
}