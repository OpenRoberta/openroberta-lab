package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MotorStopActionTest {

    @Test
    public void stopMotor() throws Exception {
        String a = "\nhal.stopRegulatedMotor(ActorPort.A, MotorStopMode.FLOAT);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_MotorStop.xml");
    }
}