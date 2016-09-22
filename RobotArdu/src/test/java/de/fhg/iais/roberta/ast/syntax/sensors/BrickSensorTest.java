package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class BrickSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nrob.buttonIsPressed(2)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml");
    }
}
