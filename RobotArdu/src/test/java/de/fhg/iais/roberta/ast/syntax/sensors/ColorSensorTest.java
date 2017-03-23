package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class ColorSensorTest {
    Helper h = new Helper();

    @Test
    public void setColor() throws Exception {
        final String a =
            "\nrob.colorSensorColor(colorsRight,3)rob.colorSensorLight(colorsLeft,1){(double)rob.colorSensorRGB(colorsRight,2)[0],(double)rob.colorSensorRGB(colorsRight,2)[1],(double)rob.colorSensorRGB(colorsRight,2)[2]}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml", false);
    }
}
