package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class UltrasonicSensorTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nSensorUS(S4)SensorUS(S2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
