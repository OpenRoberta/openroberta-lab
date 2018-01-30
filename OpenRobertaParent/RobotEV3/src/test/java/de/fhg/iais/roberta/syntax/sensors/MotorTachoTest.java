package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class MotorTachoTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void setMotorTacho() throws Exception {
        String a =
            "\nhal.getRegulatedMotorTachoValue(ActorPort.A, MotorTachoMode.ROTATION)"
                + "hal.getUnregulatedMotorTachoValue(ActorPort.D, MotorTachoMode.DEGREE)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void resetMotorTacho() throws Exception {
        String a = "\nhal.resetRegulatedMotorTacho(ActorPort.A);}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_resetEncoder.xml");
    }
}
