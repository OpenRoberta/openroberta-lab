package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class GetPowerActionTest {
    Helper h = new Helper();

    @Test
    public void getSpeed() throws Exception {
        String a = "\nhal.getRegulatedMotorSpeed(ActorPort.B)}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorGetPower.xml");
    }
}
