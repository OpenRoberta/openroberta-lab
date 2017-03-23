package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class BrickSensorTest {
    Helper h = new Helper();

    @Test
    public void isPressed() throws Exception {
        String a = "\nrob.buttonIsPressed(2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml", false);
    }
}
