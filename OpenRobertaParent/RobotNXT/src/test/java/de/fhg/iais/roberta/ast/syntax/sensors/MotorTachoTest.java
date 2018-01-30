package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class MotorTachoTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

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
