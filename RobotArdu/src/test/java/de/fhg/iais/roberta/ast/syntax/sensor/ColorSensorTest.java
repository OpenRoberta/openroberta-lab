package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class ColorSensorTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void setColor() throws Exception {
        final String a =
            "\nbnr.colorSensorColor(colorsRight,3)bnr.colorSensorLight(colorsLeft,1){(double)bnr.colorSensorRGB(colorsRight,2)[0],(double)bnr.colorSensorRGB(colorsRight,2)[1],(double)bnr.colorSensorRGB(colorsRight,2)[2]}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml", false);
    }
}
