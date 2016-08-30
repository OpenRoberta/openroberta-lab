package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        final String a =
            "\n\"COLOR\")rob.colorSensorLight(colors,1){rob.colorSensorRGB(colors,1)[0],rob.colorSensorRGB(colors,1)[1],rob.colorSensorRGB(colors,1)[2]}))";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
