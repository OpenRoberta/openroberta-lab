package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class GetPowerActionTest {
    @Test
    public void getSpeed() throws Exception {
        String a = "\nhal.getRegulatedMotorSpeed(ActorPort.B)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorGetPower.xml"));
    }
}
