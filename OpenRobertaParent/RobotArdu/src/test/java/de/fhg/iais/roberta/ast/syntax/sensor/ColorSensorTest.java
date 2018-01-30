package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class ColorSensorTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void setColor() throws Exception {
        final String a =
            "\nbnr.colorSensorColor(colorsRight,3)bnr.colorSensorLight(colorsLeft,1)bnr.colorSensorRGB(colorsRight,2)[0],bnr.colorSensorRGB(colorsRight,2)[1],bnr.colorSensorRGB(colorsRight,2)[2]";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml", false);
    }
}
