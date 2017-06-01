package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MotorOnActionTest {
    Helper h = new Helper();

    @Test
    public void motorOn() throws Exception {
        String a = "OnFwdReg(OUT_B, 30, OUT_REGMODE_SPEED);" + "OnFwdReg(OUT_C, 50, OUT_REGMODE_SPEED);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "RotateMotor(OUT_B, 30, 360 * 1);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorOnFor.xml");
    }
}