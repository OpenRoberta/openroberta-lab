package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class InfraredSensorTest {
    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.setInfraredSensorMode(S4, DISTANCE);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_setInfrared.xml"));
    }

    @Test
    public void getInfraredModeName() throws Exception {
        String a = "\nhal.getInfraredSensorModeName(S4)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getModeInfrared.xml"));
    }

    @Test
    public void getSampleInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorValue(S4)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleInfrared.xml"));
    }
}
