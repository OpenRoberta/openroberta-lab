package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorCurveActionTest extends Ev3LejosAstTest {

    @Test
    public void visitCurveAction_curveForwardThenBackward_generateValidJavaCode() throws Exception {
        String a =
            "public void run() throws Exception {"
                + "hal.driveInCurve(DriveDirection.FOREWARD, 20, 50);"
                + "hal.driveInCurve(DriveDirection.BACKWARD, 50, 20);"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_MotorCurveOn.xml", makeStandard(), false);
    }

    @Test
    public void visitCurveAction_curveForwardThenBackwardDistance_generateValidJavaCode() throws Exception {
        String a =
            "public void run() throws Exception {"
                + "hal.driveInCurve(DriveDirection.FOREWARD, 20, 50, 40);"
                + "hal.driveInCurve(DriveDirection.BACKWARD, 50, 20, 20);"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_MotorCurveOnFor.xml", makeStandard(), false);
    }
}