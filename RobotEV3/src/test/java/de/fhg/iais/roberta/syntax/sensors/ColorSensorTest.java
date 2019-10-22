package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ColorSensorTest extends Ev3LejosAstTest {

    @Test
    public void setColor() throws Exception {
        String a =
            "\nhal.getColorSensorColour(SensorPort.S3)"
                + "hal.getColorSensorRed(SensorPort.S3)"
                + "hal.getColorSensorRgb(SensorPort.S3)"
                + "hal.getColorSensorAmbient(SensorPort.S3)}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/sensors/sensor_setColor.xml", makeStandard(), false);
    }
}
