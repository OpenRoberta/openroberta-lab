package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class MotorDriveStopActionTest {
    Helper h = new Helper();

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive();}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_Stop.xml");
    }
}