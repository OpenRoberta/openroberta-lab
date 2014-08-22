package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class DriveActionTest {

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(ActorPort.A, ActorPort.B, DriveDirection.FOREWARD, 50);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorDiffOn.xml"));
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(ActorPort.A, ActorPort.B, DriveDirection.FOREWARD, 50, 20);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorDiffOnFor.xml"));
    }
}