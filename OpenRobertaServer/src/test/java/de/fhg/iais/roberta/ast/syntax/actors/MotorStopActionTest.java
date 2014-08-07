package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class MotorStopActionTest {

    @Test
    public void stopMotor() throws Exception {
        String a = "\nhal.stopRegulatedMotor(A, FLOAT);";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/actions/action_MotorStop.xml"));
    }
}