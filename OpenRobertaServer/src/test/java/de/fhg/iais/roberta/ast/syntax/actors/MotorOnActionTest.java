package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MotorOnActionTest {

    @Ignore
    public void motorOn() throws Exception {
        String a = "\nhal.regulatedDrive(ActorPort.A, ActorPort.B, DriveDirection.FOREWARD, 50);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorOn.xml"));
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "\n        hal.rotateUnregulatedMotor(ActorPort.B, 30, MotorMoveMode.ROTATIONS, 1);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorOnFor.xml"));
    }
}