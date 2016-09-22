package de.fhg.iais.roberta.ast.syntax.actors;

import de.fhg.iais.roberta.testutil.Helper;

public class DriveActionTest {
    //
    public void drive() throws Exception {
        final String a = "OnFwdReg(OUT_BC,50,OUT_REGMODE_SYNC)";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffOn.xml");
    }

    //
    public void driveFor() throws Exception {
        final String a = "\nRotateMotorEx(OUT_BC,50,Infinity*20,0,true,true)";
        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffOnFor.xml");
    }
}