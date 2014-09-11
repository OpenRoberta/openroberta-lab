package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class GyroSensorTest {

    @Test
    public void setGyro() throws Exception {
        String a = "\nhal.setGyroSensorMode(SensorPort.S2, GyroSensorMode.ANGLE);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void getGyroSensorModeName() throws Exception {
        String a = "\nhal.getGyroSensorModeName(SensorPort.S2)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getModeGyro.xml");
    }

    @Test
    public void getGyroSensorValue() throws Exception {
        String a = "\nhal.getGyroSensorValue(SensorPort.S2)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getSampleGyro.xml");
    }

    @Test
    public void resetGyroSensor() throws Exception {
        String a = "\nhal.resetGyroSensor(SensorPort.S2);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_resetGyro.xml");
    }
}
