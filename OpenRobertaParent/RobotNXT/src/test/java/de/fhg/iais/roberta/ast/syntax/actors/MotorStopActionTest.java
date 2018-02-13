package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MotorStopActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void stopMotor() throws Exception {
        final String a = "\nFloat(OUT_A);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorStop.xml");
    }
}