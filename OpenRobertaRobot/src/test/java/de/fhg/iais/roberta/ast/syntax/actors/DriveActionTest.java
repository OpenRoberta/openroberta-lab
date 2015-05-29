package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class DriveActionTest {

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(ActorPort.A, ActorPort.B, false, DriveDirection.FOREWARD, 50);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffOn.xml");
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(ActorPort.A, ActorPort.B, false, DriveDirection.FOREWARD, 50, 20);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffOnFor.xml");
    }
}