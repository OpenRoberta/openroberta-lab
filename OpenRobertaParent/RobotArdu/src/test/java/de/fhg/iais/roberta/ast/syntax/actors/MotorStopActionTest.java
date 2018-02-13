package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MotorStopActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void stopMotor() throws Exception {
        final String a = "one.stop1m(2)\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorStop.xml", false);
    }
}