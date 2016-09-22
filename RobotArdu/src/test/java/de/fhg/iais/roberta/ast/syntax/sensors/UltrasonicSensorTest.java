package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nrob.ultrasonicDistance(4)rob.ultrasonicDistance(2)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
