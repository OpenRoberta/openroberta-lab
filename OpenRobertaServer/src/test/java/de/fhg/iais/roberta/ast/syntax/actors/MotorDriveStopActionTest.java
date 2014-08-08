package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class MotorDriveStopActionTest {

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive(A, B);";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_Stop.xml"));
    }
}