package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class MotorTachoTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

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
