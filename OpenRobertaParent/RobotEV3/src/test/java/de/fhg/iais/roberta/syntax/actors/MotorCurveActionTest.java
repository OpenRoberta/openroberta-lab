package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class MotorCurveActionTest {
    Helper h = new Helper();

    @Test
    public void visitCurveAction_curveForwardThenBackward_generateValidJavaCode() throws Exception {
        String a =
            "public void run() throws Exception {"
                + "hal.driveInCurve(MoveDirection.FOREWARD, 20, 50);"
                + "hal.driveInCurve(MoveDirection.BACKWARD, 50, 20);"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorCurveOn.xml");
    }

    @Test
    public void visitCurveAction_curveForwardThenBackwardDistance_generateValidJavaCode() throws Exception {
        String a =
            "public void run() throws Exception {"
                + "hal.driveInCurve(MoveDirection.FOREWARD, 20, 50, 40);"
                + "hal.driveInCurve(MoveDirection.BACKWARD, 50, 20, 20);"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorCurveOnFor.xml");
    }
}