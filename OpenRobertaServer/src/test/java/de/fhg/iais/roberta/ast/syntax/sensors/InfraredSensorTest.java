package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class InfraredSensorTest {
    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.setInfraredSensorMode(SensorPort.S4, InfraredSensorMode.DISTANCE);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_setInfrared.xml"));
    }

    @Test
    public void getInfraredModeName() throws Exception {
        String a = "\nhal.getInfraredSensorModeName(SensorPort.S4)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getModeInfrared.xml"));
    }

    @Test
    public void getSampleInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorValue(SensorPort.S4)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleInfrared.xml"));
    }
}
