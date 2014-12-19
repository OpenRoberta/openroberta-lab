package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        String a =
            "\nPickColor.get(hal.getColorSensorValue(SensorPort.S3,ColorSensorMode.COLOUR))"
                + "hal.getColorSensorValue(SensorPort.S1, ColorSensorMode.RED)"
                + "hal.getColorSensorValue(SensorPort.S2, ColorSensorMode.RGB)"
                + "hal.getColorSensorValue(SensorPort.S4, ColorSensorMode.AMBIENTLIGHT)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
