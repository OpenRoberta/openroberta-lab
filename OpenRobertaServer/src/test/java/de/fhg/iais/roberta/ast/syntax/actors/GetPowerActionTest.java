package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class GetPowerActionTest {
    @Test
    public void getSpeed() throws Exception {
        String a = "\nhal.getRegulatedMotorSpeed(ActorPort.B)";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorGetPower.xml");
    }
}
