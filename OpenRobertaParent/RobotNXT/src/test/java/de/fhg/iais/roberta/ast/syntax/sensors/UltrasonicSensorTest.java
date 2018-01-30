package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class UltrasonicSensorTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nSensorUS(S4)SensorUS(S2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
