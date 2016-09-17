package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ColorSensorTest {

    @Test
    public void setColor() throws Exception {
        final String a = "\nSensorColor(S3,\"COLOR\")SensorColor(S1,\"LIGHT\")SensorColor(S4,\"AMBIENTLIGHT\")";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
    }
}
