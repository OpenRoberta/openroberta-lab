package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        final String a =
            "\nrob.colorSensorColor(colorsRight,3)rob.colorSensorLight(colorsLeft,1){(double)rob.colorSensorRGB(colorsRight,2)[0],(double)rob.colorSensorRGB(colorsRight,2)[1],(double)rob.colorSensorRGB(colorsRight,2)[2]}";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
