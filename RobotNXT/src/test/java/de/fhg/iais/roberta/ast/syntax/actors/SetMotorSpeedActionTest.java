package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class SetMotorSpeedActionTest {
    Helper h = new Helper();

    @Test
    public void setMotorSpeed() throws Exception {
        final String a = "__speed=abs(30)<100?30:30/abs(30)*100;OnFwdReg(OUT_B,__speed,OUT_REGMODE_SPEED);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorSetPower.xml");
    }
}