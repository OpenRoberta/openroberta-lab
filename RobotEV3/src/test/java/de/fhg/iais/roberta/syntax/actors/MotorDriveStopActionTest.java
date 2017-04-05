package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MotorDriveStopActionTest {

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive();}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_Stop.xml");
    }
}