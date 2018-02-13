package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class MotorCurveActionTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void visitCurveAction_curveForwardThenBackward_generateValidJavaCode() throws Exception {
        String a =
            "public void run() throws Exception {"
                + "hal.driveInCurve(DriveDirection.FOREWARD, 20, 50);"
                + "hal.driveInCurve(DriveDirection.BACKWARD, 50, 20);"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorCurveOn.xml");
    }

    @Test
    public void visitCurveAction_curveForwardThenBackwardDistance_generateValidJavaCode() throws Exception {
        String a =
            "public void run() throws Exception {"
                + "hal.driveInCurve(DriveDirection.FOREWARD, 20, 50, 40);"
                + "hal.driveInCurve(DriveDirection.BACKWARD, 50, 20, 20);"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorCurveOnFor.xml");
    }
}