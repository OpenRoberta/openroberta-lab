package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ColorSensorTest {
    Helper h = new Helper();

    @Test
    public void setColor() throws Exception {
        String a =
            "\nhal.getColorSensorColour(SensorPort.S3)"
                + "hal.getColorSensorRed(SensorPort.S1)"
                + "hal.getColorSensorRgb(SensorPort.S2)"
                + "hal.getColorSensorAmbient(SensorPort.S4)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_setColor.xml");
    }
}
