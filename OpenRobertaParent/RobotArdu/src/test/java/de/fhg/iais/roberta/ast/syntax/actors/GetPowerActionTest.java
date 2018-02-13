package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class GetPowerActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void getSpeed() throws Exception {
        final String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorGetPower.xml", false);
    }
}
