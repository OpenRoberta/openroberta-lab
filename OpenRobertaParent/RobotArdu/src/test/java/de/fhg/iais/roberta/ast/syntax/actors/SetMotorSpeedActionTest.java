package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class SetMotorSpeedActionTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void setMotorSpeed() throws Exception {
        final String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorSetPower.xml", false);
    }
}