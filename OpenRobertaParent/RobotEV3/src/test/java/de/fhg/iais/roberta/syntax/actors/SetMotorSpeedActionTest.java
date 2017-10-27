package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class SetMotorSpeedActionTest {
    Helper h = new Helper();

    @Test
    public void setMotorSpeed() throws Exception {
        String a = "\nhal.setRegulatedMotorSpeed(ActorPort.B, 30);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorSetPower.xml");
    }
}