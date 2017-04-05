package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class GetPowerActionTest {
    @Test
    public void getSpeed() throws Exception {
        String a = "\nhal.getRegulatedMotorSpeed(ActorPort.B)}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_MotorGetPower.xml");
    }
}
