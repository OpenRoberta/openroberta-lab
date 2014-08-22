package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        String a = "\nhal.setColorSensorMode(S3, COLOUR);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getColorModeName() throws Exception {
        String a = "\nhal.getColorSensorModeName(S3)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getModeColor.xml"));
    }

    @Test
    public void getSampleColor() throws Exception {
        String a = "\nhal.getColorSensorValue(S3)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleColor.xml"));
    }
}
