package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.Helper;

@Ignore
public class DriveActionTest {
    Helper h = new Helper();

    @Test
    public void drive() throws Exception {
        final String a = "OnFwdReg(OUT_BC,50,OUT_REGMODE_SYNC)";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffOn.xml", false);
    }

    @Test
    public void driveFor() throws Exception {
        final String a = "\nRotateMotorEx(OUT_BC,50,Infinity*20,0,true,true)";
        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffOnFor.xml", false);
    }
}