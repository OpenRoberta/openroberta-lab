package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorTachoTest extends Ev3LejosAstTest {

    @Test
    public void setMotorTacho() throws Exception {
        String a =
            "\nhal.getRegulatedMotorTachoValue(ActorPort.A, MotorTachoMode.ROTATION)"
                + "hal.getUnregulatedMotorTachoValue(ActorPort.D, MotorTachoMode.DEGREE)}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/sensors/sensor_setEncoder.xml",
                makeRotateRegulatedUnregulatedForwardBackwardMotors(),
                false);
    }

    @Test
    public void resetMotorTacho() throws Exception {
        String a = "\nhal.resetRegulatedMotorTacho(ActorPort.A);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/sensors/sensor_resetEncoder.xml",
                makeRotateRegulatedUnregulatedForwardBackwardMotors(),
                false);
    }
}
