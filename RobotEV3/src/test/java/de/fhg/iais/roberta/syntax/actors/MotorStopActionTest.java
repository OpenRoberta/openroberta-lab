package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class MotorStopActionTest {
    Helper h = new Helper();

    @Test
    public void stopMotor() throws Exception {
        String a = "\nhal.stopRegulatedMotor(ActorPort.A, MotorStopMode.FLOAT);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorStop.xml");
    }
}