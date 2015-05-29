package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        String a =
            "\nhal.getColorSensorColour(SensorPort.S3)"
                + "hal.getColorSensorRed(SensorPort.S1)"
                + "hal.getColorSensorRgb(SensorPort.S2)"
                + "hal.getColorSensorAmbient(SensorPort.S4)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
