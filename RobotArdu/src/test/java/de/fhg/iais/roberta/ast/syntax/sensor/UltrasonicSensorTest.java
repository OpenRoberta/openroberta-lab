package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class UltrasonicSensorTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nrob.ultrasonicDistance(4)rob.ultrasonicDistance(2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml", false);
    }
}
