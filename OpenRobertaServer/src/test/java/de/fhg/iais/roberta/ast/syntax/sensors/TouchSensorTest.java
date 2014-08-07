package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class TouchSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(S1)";

        Assert.assertEquals(a, Helper.generateSyntax("/ast/sensors/sensor_Touch.xml"));
    }
}
