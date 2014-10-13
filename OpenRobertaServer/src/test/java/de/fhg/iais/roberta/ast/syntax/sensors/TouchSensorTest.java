package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TouchSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_Touch.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_Touch.xml");
    }

}
