package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class BrickSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(BrickKey.ENTER)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml");
    }
}
