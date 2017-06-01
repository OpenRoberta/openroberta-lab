package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MotorTachoTest {
    Helper h = new Helper();

    @Test
    public void setMotorTacho() throws Exception {
        final String a = "\nMotorTachoCount(OUT_A)/360.0" + "MotorTachoCount(OUT_D)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void resetMotorTacho() throws Exception {
        final String a = "\nResetTachoCount(OUT_A);";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_resetEncoder.xml");
    }
}
