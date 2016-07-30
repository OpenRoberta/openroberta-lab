package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nSensorUS(IN_4)SensorUS(IN_2)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
