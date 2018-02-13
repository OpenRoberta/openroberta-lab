package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class SetMotorSpeedActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void setMotorSpeed() throws Exception {
        final String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorSetPower.xml", false);
    }
}