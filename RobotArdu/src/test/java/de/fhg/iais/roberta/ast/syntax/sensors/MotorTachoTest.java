package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class MotorTachoTest {
    Helper h = new Helper();

    @Test
    public void setMotorTacho() throws Exception {
        final String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setEncoder.xml", false);
    }

    @Test
    public void resetMotorTacho() throws Exception {
        final String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_resetEncoder.xml", false);
    }
}
