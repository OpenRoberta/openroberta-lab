package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TouchSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_Touch.xml"));
    }
}
