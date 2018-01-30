package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class MotorDriveStopActionTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive();}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_Stop.xml");
    }
}