package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MotorDriveStopActionTest {

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive(ActorPort.A, ActorPort.B);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_Stop.xml"));
    }
}