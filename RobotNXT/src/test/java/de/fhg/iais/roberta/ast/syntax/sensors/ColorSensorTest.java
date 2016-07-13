package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        final String a = "\nSensor(IN_3)Sensor(IN_1)Sensor(IN_2)Sensor(IN_4)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
