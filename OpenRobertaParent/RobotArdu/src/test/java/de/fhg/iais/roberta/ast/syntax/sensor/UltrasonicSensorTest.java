package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class UltrasonicSensorTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nbnr.ultrasonicDistance(4)bnr.ultrasonicDistance(2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml", false);
    }
}
