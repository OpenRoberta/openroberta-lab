package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MotorOnActionTest {
    Helper h = new Helper();

    @Test
    public void motorOn() throws Exception {
        String a =
            "__speed=abs(30)<100?30:30/abs(30)*100;OnFwdReg(OUT_B,__speed,OUT_REGMODE_SPEED);__speed=abs(50)<100?50:50/abs(50)*100;OnFwdReg(OUT_C, __speed, OUT_REGMODE_SPEED);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "__speed=abs(30)<100?30:30/abs(30)*100;RotateMotor(OUT_B,__speed, 360 * 1);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorOnFor.xml");
    }
}