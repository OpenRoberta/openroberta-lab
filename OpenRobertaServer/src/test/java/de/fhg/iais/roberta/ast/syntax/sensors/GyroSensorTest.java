package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class GyroSensorTest {

    @Test
    public void setGyro() throws Exception {
        String a = "\nhal.setGyroSensorMode(S2, ANGLE);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_setGyro.xml"));
    }

    @Test
    public void getGyroSensorModeName() throws Exception {
        String a = "\nhal.getGyroSensorModeName(S2)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getModeGyro.xml"));
    }

    @Test
    public void getGyroSensorValue() throws Exception {
        String a = "\nhal.getGyroSensorValue(S2)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleGyro.xml"));
    }

    @Test
    public void resetGyroSensor() throws Exception {
        String a = "\nhal.resetGyroSensor(S2);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_resetGyro.xml"));
    }
}
