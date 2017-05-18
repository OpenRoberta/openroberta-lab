package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class SetMotorSpeedActionTest {
    Helper h = new Helper();

    @Test
    public void setMotorSpeed() throws Exception {
        final String a = "OnReg(OUT_B,30,OUT_REGMODE_SPEED);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorSetPower.xml");
    }
}