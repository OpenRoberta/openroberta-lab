package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        String a =
            "\nhal.getUltraSonicSensorValue(SensorPort.S4, UltrasonicSensorMode.DISTANCE)"
                + "hal.getUltraSonicSensorValue(SensorPort.S2, UltrasonicSensorMode.PRESENCE)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
