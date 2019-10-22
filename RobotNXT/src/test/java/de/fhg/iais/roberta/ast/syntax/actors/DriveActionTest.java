package de.fhg.iais.roberta.ast.syntax.actors;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DriveActionTest extends NxtAstTest {

    //
    public void drive() throws Exception {
        final String a = "OnFwdReg(OUT_BC,50,OUT_REGMODE_SYNC)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorDiffOn.xml", false);
    }

    //
    public void driveFor() throws Exception {
        final String a = "\nRotateMotorEx(OUT_BC,50,Infinity*20,0,true,true)";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorDiffOnFor.xml", false);
    }
}