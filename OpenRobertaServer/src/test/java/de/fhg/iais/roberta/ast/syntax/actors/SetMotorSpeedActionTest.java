package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class SetMotorSpeedActionTest {

    @Test
    public void setMotorSpeed() throws Exception {
        String a = "\nhal.setRegulatedMotorSpeed(B, 30);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorSetPower.xml"));
    }
}