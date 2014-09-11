package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        String a = "\nhal.setColorSensorMode(SensorPort.S3, ColorSensorMode.COLOUR);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }

    @Test
    public void getColorModeName() throws Exception {
        String a = "\nhal.getColorSensorModeName(SensorPort.S3)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getModeColor.xml");
    }

    @Test
    public void getSampleColor() throws Exception {
        String a = "\nhal.getColorSensorValue(SensorPort.S3)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getSampleColor.xml");
    }
}
