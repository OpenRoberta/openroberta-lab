package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MotorTachoTest {

    @Test
    public void setMotorTacho() throws Exception {
        String a =
            "\nhal.getRegulatedMotorTachoValue(ActorPort.A, MotorTachoMode.ROTATION)" + "hal.getUnregulatedMotorTachoValue(ActorPort.D, MotorTachoMode.DEGREE)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void resetMotorTacho() throws Exception {
        String a = "\nhal.resetRegulatedMotorTacho(ActorPort.A);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_resetEncoder.xml");
    }
}
