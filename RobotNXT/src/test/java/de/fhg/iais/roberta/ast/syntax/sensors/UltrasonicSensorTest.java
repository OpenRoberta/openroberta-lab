package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nSensorUS(S4)SensorUS(S2)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
