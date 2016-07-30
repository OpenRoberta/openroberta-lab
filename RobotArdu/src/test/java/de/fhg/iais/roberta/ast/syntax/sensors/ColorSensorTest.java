package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_3,\"COLOR\")SensorColor(IN_1,\"LIGHT\")SensorColor(IN_2,)SensorColor(IN_4,\"AMBIENTLIGHT\")";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
