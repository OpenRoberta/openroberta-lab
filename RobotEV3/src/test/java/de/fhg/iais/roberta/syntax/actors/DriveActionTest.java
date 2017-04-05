package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class DriveActionTest {

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(DriveDirection.FOREWARD, 50);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffOn.xml");
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(DriveDirection.FOREWARD, 50, 20);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffOnFor.xml");
    }
}