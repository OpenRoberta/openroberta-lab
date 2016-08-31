package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MotorTachoTest {

    @Test
    public void setMotorTacho() throws Exception {
        final String a = "\n";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void resetMotorTacho() throws Exception {
        final String a = "\n";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_resetEncoder.xml");
    }
}
