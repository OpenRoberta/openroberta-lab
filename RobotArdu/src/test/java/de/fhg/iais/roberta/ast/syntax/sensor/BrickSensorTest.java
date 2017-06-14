package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class BrickSensorTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void isPressed() throws Exception {
        String a = "\nbnr.buttonIsPressed(2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml", false);
    }
}
