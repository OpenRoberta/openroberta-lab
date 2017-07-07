package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MotorTachoTest {
    HelperBotNroll h = new HelperBotNroll();

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
