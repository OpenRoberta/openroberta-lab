package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        String a = "\nhal.setColorSensorMode(SensorPort.S3, ColorSensorMode.COLOUR);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getColorModeName() throws Exception {
        String a = "\nhal.getColorSensorModeName(SensorPort.S3)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getModeColor.xml"));
    }

    @Test
    public void getSampleColor() throws Exception {
        String a = "\nhal.getColorSensorValue(SensorPort.S3)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleColor.xml"));
    }
}
