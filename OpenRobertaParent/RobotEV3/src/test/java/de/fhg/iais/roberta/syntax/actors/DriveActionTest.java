package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class DriveActionTest {
    Helper h = new Helper();

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(MoveDirection.FOREWARD, 50);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffOn.xml");
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(MoveDirection.FOREWARD, 50, 20);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffOnFor.xml");
    }
}