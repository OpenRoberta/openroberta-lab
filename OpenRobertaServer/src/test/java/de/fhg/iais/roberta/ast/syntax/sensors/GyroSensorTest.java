package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class GyroSensorTest {

    @Test
    public void setGyro() throws Exception {
        String a = "\nhal.getGyroSensorValue(SensorPort.S2, GyroSensorMode.ANGLE)" + "hal.getGyroSensorValue(SensorPort.S4, GyroSensorMode.RATE)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void resetGyroSensor() throws Exception {
        String a = "\nhal.resetGyroSensor(SensorPort.S2);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_resetGyro.xml");
    }
}
