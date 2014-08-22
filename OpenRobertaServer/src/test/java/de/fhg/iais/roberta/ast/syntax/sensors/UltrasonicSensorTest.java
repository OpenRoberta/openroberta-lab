package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class UltrasonicSensorTest {
    @Test
    public void setUltrasonic() throws Exception {
        String a = "\nhal.setUltrasonicSensorMode(S4, DISTANCE);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_setUltrasonic.xml"));
    }

    @Test
    public void getUltrasonicModeName() throws Exception {
        String a = "\nhal.getUltraSonicSensorModeName(S4)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getModeUltrasonic.xml"));
    }

    @Test
    public void getSampleUltrasonic() throws Exception {
        String a = "\nhal.getUltraSonicSensorValue(S4)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleUltrasonic.xml"));
    }
}
