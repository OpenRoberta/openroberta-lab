package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ColorSensorTest extends BotnrollAstTest {

    @Test
    public void setColor() throws Exception {
        final String a =
            "\nbnr.colorSensorColor(colorsRight,3)bnr.colorSensorLight(colorsLeft,1) {bnr.colorSensorRGB(colorsRight,2)[0],bnr.colorSensorRGB(colorsRight,2)[1],bnr.colorSensorRGB(colorsRight,2)[2]}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setColor.xml", makeConfiguration(), false);
    }
}
