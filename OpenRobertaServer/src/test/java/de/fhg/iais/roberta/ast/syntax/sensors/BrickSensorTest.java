package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class BrickSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(BrickKey.ENTER)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_brick1.xml"));
    }

    @Test
    public void isPressedAndReleased() throws Exception {
        String a = "\nhal.isPressedAndReleased(BrickKey.ENTER)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_brick2.xml"));
    }
}
